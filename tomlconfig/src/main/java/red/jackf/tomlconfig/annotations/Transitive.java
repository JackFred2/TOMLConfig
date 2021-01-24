package red.jackf.tomlconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Marks a class to be serialised as-is under a sub header, without any special serialization method.
 * Useful for record-like objects, or modularising a config. Can be applied to a field or a class as a whole.</p>
 *
 * <p>If no mapping is available for a class or field marked with {@link Transitive}, then there <i>must</i> be a nullary
 * constructor available.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Transitive {
}
