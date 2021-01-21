package red.jackf.tomlconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a class is a configuration file. Only needs to be defined for a top level class.
 */
public interface Config {

    /**
     * Used for defining additional properties for the config, such as a file name or loading methods.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface Data {
        /**
         * If used in the top level class, it defines a custom file name for the config.
         */
        String name();
    }

    final class Field {
        /**
         * Adds a TOML comment before the field. Will be broken into separate lines if needed.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.FIELD)
        public @interface Comment {
            String value();
        }
    }
}
