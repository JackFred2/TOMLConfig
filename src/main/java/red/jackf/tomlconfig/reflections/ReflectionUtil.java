package red.jackf.tomlconfig.reflections;

import java.lang.reflect.Constructor;

public abstract class ReflectionUtil {
    /**
     * Create a new instance of a class. Assumes that a no-args constructor exists. Throws a {@link RuntimeException} if
     * no no-arg constructor exists.
     *
     * @param clazz Class value to instantiate.
     * @param <C>   Type parameter of the class to instantiate.
     * @return A new instance of {@code clazz}
     */
    public static <C> C instantiate(Class<C> clazz) {
        try {
            Constructor<C> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor available for " + clazz.getName() + ", ensure it has a 0-arg constructor available.");
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
