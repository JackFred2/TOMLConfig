package red.jackf.tomlconfig.reflections;

import red.jackf.tomlconfig.annotations.Config;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.parser.data.TOMLKey;
import red.jackf.tomlconfig.parser.data.TOMLTable;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;
import red.jackf.tomlconfig.reflections.mapping.base.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for taking an {@link Config} object and populating the
 * fields with a given {@link TOMLTable} object.
 */
public class ClassPopulator {
    public static final ClassPopulator INSTANCE = new ClassPopulator();

    private final Map<Class<?>, Mapping<?>> mappings = new HashMap<>();

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
    }

    public <T> void register(Class<T> clazz, Mapping<T> mapping) {
        mappings.put(clazz, mapping);
    }

    @SuppressWarnings("unchecked")
    public <T> Mapping<T> getMapping(Class<T> clazz) {
        if (clazz.isArray()) return (Mapping<T>) ARRAY_MAPPING;
        return (Mapping<T>) mappings.get(clazz);
    }

    /**
     * Convert a whole TOMLTable to a class definition.
     */
    public <C> C toObject(Class<C> clazz, TOMLTable root) throws ReflectiveOperationException, ParsingException {
        C object = instantiate(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                String name = field.getName();
                TOMLValue data = root.getData(new TOMLKey(name));
                if (data != null) { // non-existing fields will be left at default.
                    Object fieldObject = createObject(data, field.getGenericType());
                    field.set(object, fieldObject);
                }
            }
        }
        
        return object;
    }

    public TOMLValue fromObject(Object rootObject) throws ReflectiveOperationException, ParsingException {
        TOMLTable root = new TOMLTable();
        Field[] fields = rootObject.getClass().getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                String name = field.getName();
                if (field.get(rootObject) != null) { // null fields will not be added.
                    TOMLValue fieldValue = createTOMLValue(field.get(rootObject));
                    root.addData(new TOMLKey(name), fieldValue);
                }
            }
        }

        return root;
    }

    /**
     *  Converts a single TOMLValue object and Type to an Object.
     *  */
    public Object createObject(TOMLValue data, Type genericType) throws ParsingException {
        Mapping<?> mapping;
        if (genericType instanceof Class<?>) {
            Class<?> clazz = (Class<?>) genericType;
            mapping = getMapping(clazz);
            if (mapping == null) {
                throw new ParsingException("No mapping registered for type " + clazz);
            }
        } else {
            throw new ParsingException("Unsupported: " + genericType.getClass().getSimpleName());
        }
        return mapping.toObject(data, genericType);
    }

    public <T> TOMLValue createTOMLValue(T object) throws ParsingException {
        //noinspection unchecked
        Mapping<T> mapping = getMapping((Class<T>) object.getClass());
        if (mapping == null) {
            throw new ParsingException("No mapping registered for type " + object.getClass());
        }
        return mapping.fromObject(object);
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
