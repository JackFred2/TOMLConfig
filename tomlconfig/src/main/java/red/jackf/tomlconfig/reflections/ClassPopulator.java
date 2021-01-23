package red.jackf.tomlconfig.reflections;

import red.jackf.tomlconfig.annotations.Config;
import red.jackf.tomlconfig.annotations.Transitive;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.parser.data.TOMLKey;
import red.jackf.tomlconfig.parser.data.TOMLTable;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;
import red.jackf.tomlconfig.reflections.mapping.base.*;
import red.jackf.tomlconfig.reflections.mapping.util.*;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for taking an {@link Config} object and populating the
 * fields with a given {@link TOMLTable} object.
 */
public class ClassPopulator {
    public static final ClassPopulator INSTANCE = new ClassPopulator();

    private final Map<Type, Mapping<?>> mappings = new HashMap<>();

    private static final Mapping<Object> ARRAY_MAPPING = new ArrayMapping();

    private ClassPopulator() {
        register(boolean.class, new BooleanMapping());
        register(Boolean.class, new BooleanMapping());
        register(byte.class, new ByteMapping());
        register(Byte.class, new ByteMapping());
        register(char.class, new CharMapping());
        register(Character.class, new CharMapping());
        register(double.class, new DoubleMapping());
        register(Double.class, new DoubleMapping());
        register(float.class, new FloatMapping());
        register(Float.class, new FloatMapping());
        register(int.class, new IntegerMapping());
        register(Integer.class, new IntegerMapping());
        register(long.class, new LongMapping());
        register(Long.class, new LongMapping());
        register(short.class, new ShortMapping());
        register(Short.class, new ShortMapping());
        register(String.class, new StringMapping());

        register(List.class, new ListMapping());
        register(Set.class, new SetMapping());
        register(Map.class, new MapMapping());
        register(LocalTime.class, new LocalTimeMapping());
        register(LocalDate.class, new LocalDateMapping());
        register(LocalDateTime.class, new LocalDateTimeMapping());
        register(OffsetDateTime.class, new OffsetDateTimeMapping());
    }

    public void register(Class<?> clazz, Mapping<?> mapping) {
        mappings.put(clazz, mapping);
    }

    public Boolean hasMapping(Type type) {
        return getMapping(type) != null;
    }

    public Mapping<?> getMapping(Type type) {
        Class<?> clazz;
        if (type instanceof Class<?>) clazz = (Class<?>) type;
        else if (type instanceof ParameterizedType) clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        else throw new UnsupportedOperationException("Unsupported type " + type);
        if (clazz.isArray()) return ARRAY_MAPPING;
        for (Type key : mappings.keySet()) {
            if (key.equals(clazz)) {
                return mappings.get(key);
            }
        }

        // interfaces
        for (Type key : mappings.keySet()) {
            if (key instanceof Class<?>)
                if (((Class<?>) key).isAssignableFrom(clazz)) {
                    return mappings.get(key);
                }
        }
        return null;
    }

    /**
     * Convert a whole TOMLTable to an object based off a class definition.
     */
    public Object toObject(Type type, TOMLValue value) throws ReflectiveOperationException, ParsingException {
        if (hasMapping(type)) {
            return createObject(value, type);
        } else {
            TOMLTable root = (TOMLTable) value;
            Class<?> clazz;
            if (type instanceof Class<?>) clazz = (Class<?>) type;
            else if (type instanceof ParameterizedType) clazz = (Class<?>) ((ParameterizedType) type).getRawType();
            else throw new UnsupportedOperationException("Unsupported type " + type);
            Object object = instantiate(clazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                boolean modifiedAccessible = false;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    modifiedAccessible = true;
                }
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    String name = field.getName();
                    TOMLValue data = root.getData(new TOMLKey(name));
                    if (data != null) { // non-existing fields will be left at default.
                        Object fieldObject;
                        if (field.getType().isAnnotationPresent(Transitive.class)) {
                            fieldObject = toObject(field.getType(), data);
                        } else {
                            fieldObject = createObject(data, field.getGenericType());
                        }

                        Config.FieldData fieldData = field.getAnnotation(Config.FieldData.class);

                        // try and find a setter for the field.
                        boolean set = false;
                        if (fieldData != null && !fieldData.setter().equals("")) {
                            for (Method method : clazz.getMethods()) {
                                if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(fieldData.setter()) && method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(field.getType())) {
                                    method.invoke(object, fieldObject);
                                    set = true;
                                    break;
                                }
                            }
                            if (!set)
                                throw new ParsingException("Could not find setter method matching @Config.FieldData name of " + fieldData.setter());
                        } else {
                            for (Method method : clazz.getMethods()) {
                                if (Modifier.isPublic(method.getModifiers()) && method.getName().toLowerCase().equals("set" + name.toLowerCase()) && method.getReturnType() == Void.TYPE && method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(field.getType())) {
                                    method.invoke(object, fieldObject);
                                    set = true;
                                }
                            }
                            // no setter, set directly
                            if (!set) field.set(object, fieldObject);
                        }
                    }
                }
                if (modifiedAccessible) {
                    field.setAccessible(false);
                }
            }

            return object;
        }
    }

    public TOMLValue fromObject(Object rootObject) throws ParsingException, ReflectiveOperationException {
        if (hasMapping(rootObject.getClass())) {
            return createTOMLValue(rootObject);
        } else {
            TOMLTable root = new TOMLTable();
            Field[] fields = rootObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean modifiedAccessible = false;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    modifiedAccessible = true;
                }
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    String name = field.getName();
                    if (field.get(rootObject) != null) { // null fields will not be added.
                        TOMLValue fieldValue = fromObject(field.get(rootObject));
                        root.addData(new TOMLKey(name), fieldValue);
                    }
                }
                if (modifiedAccessible) field.setAccessible(false);
            }

            return root;
        }
    }

    /**
     *  Converts a single TOMLValue and Type to an Object. Assumes that a mapping exists.
     *  */
    public Object createObject(TOMLValue data, Type genericType) throws ParsingException {
        Mapping<?> mapping;
        mapping = getMapping(genericType);
        if (mapping == null) {
            throw new ParsingException("No mapping registered for type " + genericType);
        }
        return mapping.toObject(this, data, genericType);
    }

    /**
     * Creates a TOMLValue from an object. Assumes that a mapping exists.
     */
    public TOMLValue createTOMLValue(Object object) throws ParsingException {
        Mapping<?> mapping = getMapping(object.getClass());
        if (mapping == null) {
            throw new ParsingException("No mapping registered for type " + object.getClass());
        }
        return mapping.fromObject(this, object);
    }

    private static <C> C instantiate(Class<C> clazz) throws ReflectiveOperationException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                //noinspection unchecked
                return (C) constructor.newInstance();
            }
        }
        throw new IllegalStateException("No constructor available for " + clazz.getName());
    }
}
