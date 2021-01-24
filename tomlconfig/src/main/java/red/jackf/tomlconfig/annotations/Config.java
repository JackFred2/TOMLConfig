package red.jackf.tomlconfig.annotations;

import red.jackf.tomlconfig.data.TOMLValue;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * <p>Specifies that a class is a configuration file. Only needs to be defined for a top level class.</p>
 * <p>A Config class requires a zero-arg constructor.</p>
 */
public interface Config {

    /**
     * Get the file name of the config. By default, this is the class name.
     * @return File name of the config - will have ".toml" appended.
     */
    default String fileName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Gets the folder where the config file will be stored. By default, this is the program's working directory.
     * @return Path of the configuration directory.
     */
    default Path getDirectory() {
        return FileSystems.getDefault().getPath("");
    }

    /**
     * Called when a config is successfully deserialized.
     */
    default void onLoad() {}

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
     * <p>Default behavior is to look for a method called {@code set<FIELDNAME>}; for example for a field named {@code permissions},
     * it will look for a method called {@code setPermissions(...)}. If one is not found, it will attempt to directly set the
     * field.</p>
     * <p>Using this annotation causes the parser to <i>expect</i> the method, and to throw if not found.</p>
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
     * defined in {@link red.jackf.tomlconfig.writer.TOMLWriter}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Comment {
        String value();
    }
}
