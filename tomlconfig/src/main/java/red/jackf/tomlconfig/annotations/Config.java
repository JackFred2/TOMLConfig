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
     * Called when a config is successfully deserialized.
     */
    default void onLoad() {}
}
