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
     * Defines various behaviors for a field.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface FieldData {
        /**
         * Defines a comment to be printed before the field. Will be broken into multiple lines if necessary
         */
        String comment() default "";
        /**
         * Defines a custom setter method to search for to be used when setting the field. Must be public, and take 1
         * argument of the type of the field. <br />
         * Default behavior is to use the field name with "set" prepended, or to directly set the field if not found;
         * setting this field causes loading to fail if the method is not found.
         */
        String setter() default "";
    }

    /**
     * Marks a field without a loaded mapping to be treated as a sub-config (think partitioning the config into
     * sub-objects).
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Transitive {}

    /**
     * Called when a config is successfully deserialized.
     */
    default void onLoad() {}
}
