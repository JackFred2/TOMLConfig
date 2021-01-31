package red.jackf.tomlconfig.annotations;

import red.jackf.tomlconfig.data.TOMLValue;

import java.lang.annotation.*;
import java.lang.reflect.Type;

/**
 * <p>Specifies that a class is a configuration file. Only needs to be defined for a top level class.</p>
 * <p>A Config class requires a zero-arg constructor.</p>
 */
public interface Config {

    /**
     * Called when a config is successfully deserialized.
     */
    default void onLoad() {
    }

    /**
     * <p>Marks a class to be serialised as-is under a sub header, without any special serialization method.
     * Useful for record-like objects, or modularising a config. Can be applied to a field or a class as a whole.</p>
     *
     * <p>If no mapping is available for a class or field marked with {@link Transitive}, then there <i>must</i> be a nullary
     * constructor available.</p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @interface Transitive {
    }

    /**
     * <p>Defines a custom setter method to search for to be used when setting the field. Must be public, and take 1
     * argument of the type of the field.</p>
     * <p>Default behavior (without {@code @Setter}) is to look for a method called {@code set<FIELDNAME>}; for example for a field named {@code permissions},
     * it will look for a method called {@code setPermissions(...)}. If one is not found, it will attempt to directly set the
     * field.</p>
     * <p>Using this annotation changes behavior: if the value is the empty string {@code ""}, then it will always
     * attempt to set the field directly ignoring any setters. Otherwise it will expect a setter with the given name,
     * and throw if not found.</p>
     *
     * @see red.jackf.tomlconfig.reflections.ClassPopulator#toObject(Type, TOMLValue)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Setter {
        String value();
    }

    /**
     * Defines a comment to be printed before a field. Will be broken into multiple lines if necessary, length of which is
     * defined at {@link red.jackf.tomlconfig.TOMLConfig.Builder#withMaxLineLength(int)}. You may use this annotation multiple
     * times, which will result in lines being concatenated with a newline.
     */
    @Repeatable(Comment.CommentsList.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Comment {
        String value();

        /**
         * Internal class for repeated comment annotations. You can use this, but it is preferred to repeat the
         * above annotation.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.FIELD)
        @interface CommentsList {
            Comment[] value();
        }
    }

    /**
     * Alternative to the Java {@code transient} keyword; any fields with this annotation will not be written to a
     * config file or read from a file. In most cases, the {@code transient} keyword is preferred to this annotation.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Transient {
    }
}
