package red.jackf.tomlconfig.reflections;

import red.jackf.tomlconfig.annotations.Comment;
import red.jackf.tomlconfig.annotations.Config;
import red.jackf.tomlconfig.annotations.Setter;
import red.jackf.tomlconfig.annotations.Transitive;
import red.jackf.tomlconfig.data.TOMLKey;
import red.jackf.tomlconfig.data.TOMLTable;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.reflections.mapping.Mapping;
import red.jackf.tomlconfig.reflections.mapping.base.*;
import red.jackf.tomlconfig.reflections.mapping.util.*;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * This class is responsible for taking an {@link Config} object and populating the
 * fields with a given {@link TOMLTable} object.
 */
public class ClassPopulator {
    private final Map<Type, Mapping<?>> mappings = new HashMap<>();

    private static final Mapping<Object> ARRAY_MAPPING = new ArrayMapping();

    /**
     * Create a new ClassPopulator, with all default mappings loaded. Additional mappings for custom objects can be
     * registered if necessary.
     *
     * @see ClassPopulator#register(Class, Mapping)
     */
    public ClassPopulator() {
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

    /**
     * Register a custom mapping to map a class to TOML, or vice versa. Supports interfaces as well as classes; a direct
     * mapping will be picked over superclass mappings (e.g. if a mapping is defined for both {@link HashMap} and
     * {@link Map}, then serializing a {@link HashMap} object will use the {@link HashMap} mapping, whereas serializing a
     * {@link TreeMap} will use the {@link Map} mapping.
     *
     * @param clazz   Class that the mapping supports;
     * @param mapping An instance of a {@link Mapping} object;
     */
    public void register(Class<?> clazz, Mapping<?> mapping) {
        mappings.put(clazz, mapping);
    }

    /**
     * Whether this {@link ClassPopulator} supports serializing the given type.
     *
     * @param type The type (usually a class, but can be an interface) to check a mapping for.
     * @return Whether this ClassPopulator has a mapping available for {@code type}
     */
    public Boolean hasMapping(Type type) {
        return getMapping(type) != null;
    }

    /**
     * Retrieves a mapping that matches the given type, either directly or as a superclass.
     *
     * @param type The type (usually a class, but can be an interface) to look up a mapping for
     * @return The mapping, or null if none match
     */
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
     * Convert a whole TOMLTable to an object based off a class definition. Uses a mapping if available, or tries to
     * populate the fields manually if not. Use this method to recursively deserialize objects if writing your own
     * {@link Mapping}.
     *
     * @param type  The type to model {@code value} as
     * @param value The {@link TOMLValue} node to parse.
     * @throws ParsingException If a the object could not be populated
     */
    public Object toObject(Type type, TOMLValue value) throws ParsingException {
        try {
            if (hasMapping(type)) {
                return createObject(type, value);
            } else {
                TOMLTable root = (TOMLTable) value;
                Class<?> clazz;
                if (type instanceof Class<?>) clazz = (Class<?>) type;
                else if (type instanceof ParameterizedType) clazz = (Class<?>) ((ParameterizedType) type).getRawType();
                else throw new UnsupportedOperationException("Unsupported type " + type);
                Object object = instantiate(clazz);
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                        String name = field.getName();
                        TOMLValue data = root.getData(new TOMLKey(name));
                        if (data != null) { // non-existing fields will be left at default.
                            Object fieldObject;
                            if (field.getType().isAnnotationPresent(Transitive.class) || field.isAnnotationPresent(Transitive.class)) {
                                fieldObject = toObject(field.getType(), data);
                            } else {
                                fieldObject = createObject(field.getGenericType(), data);
                            }

                            Setter setter = field.getAnnotation(Setter.class);

                            // try and find a setter for the field.
                            boolean set = false;
                            if (setter != null) {
                                for (Method method : clazz.getMethods()) {
                                    if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(setter.value()) && method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(field.getType())) {
                                        method.invoke(object, fieldObject);
                                        set = true;
                                        break;
                                    }
                                }
                                if (!set)
                                    throw new ParsingException("Could not find designated setter method matching @Setter name of " + setter.value());
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
                    field.setAccessible(false);
                }

                return object;
            }
        } catch (ReflectiveOperationException ex) {
            throw new ParsingException(ex);
        }
    }

    /**
     * Serialize an arbitrary object into a TOMLValue. Use this method to recursively serialize objects if writing your
     * own {@link Mapping}.
     *
     * @param object The object to serialize.
     * @return TOML representation of the object.
     * @throws ParsingException If the object could not be serialized.
     */
    public TOMLValue fromObject(Object object) throws ParsingException {
        try {
            if (hasMapping(object.getClass())) {
                return createTOMLValue(object);
            } else {
                TOMLTable root = new TOMLTable();
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                        String name = field.getName();
                        if (field.get(object) != null) { // null fields will not be added.
                            TOMLValue fieldValue = fromObject(field.get(object));
                            Comment comment = field.getAnnotation(Comment.class);
                            if (comment != null) {
                                fieldValue.setComment(comment.value());
                            }
                            root.addData(new TOMLKey(name), fieldValue);
                        }
                    }
                    field.setAccessible(false);
                }

                return root;
            }
        } catch (ReflectiveOperationException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Converts a single TOMLValue and Type to an Object, <i>assuming a mapping exists</i>.
     */
    private Object createObject(Type type, TOMLValue data) throws ParsingException {
        Mapping<?> mapping;
        mapping = getMapping(type);
        if (mapping == null) {
            throw new ParsingException("No mapping registered for type " + type);
        }
        return mapping.toObject(this, type, data);
    }

    /**
     * Creates a TOMLValue from an object, <i>assuming a mapping exists</i>.
     */
    private TOMLValue createTOMLValue(Object object) throws ParsingException {
        Mapping<?> mapping = getMapping(object.getClass());
        if (mapping == null) {
            throw new ParsingException("No mapping registered for type " + object.getClass());
        }
        return mapping.fromObject(this, object);
    }

    /**
     * Create a new instance of a class. Assumes that a no-args constructor exists.
     *
     * @throws ReflectiveOperationException If instantiating the object causes an error.
     * @throws IllegalStateException        If no no-args constructor exists.
     */
    private static <C> C instantiate(Class<C> clazz) throws ReflectiveOperationException {
        @SuppressWarnings("unchecked")
        Constructor<C>[] constructors = (Constructor<C>[]) clazz.getDeclaredConstructors();
        for (Constructor<C> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor.newInstance();
            }
        }
        throw new IllegalStateException("No default constructor available for " + clazz.getName() + ", ensure it has a 0-arg constructor available.");
    }
}
