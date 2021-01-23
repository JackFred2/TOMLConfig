package red.jackf.tomlconfig.annotations;

import red.jackf.tomlconfig.data.TOMLValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

/**
 * Defines a custom setter method to search for to be used when setting the field. Must be public, and take 1
 * argument of the type of the field.
 * Default behavior is to look for a method called {@code set<FIELDNAME>}; for example for a field named {@code permissions},
 * it will look for a method called {@code setPermissions(...)}. If one is not found, it will attempt to directly set the
 * field. <br />
 * Using this annotation causes the parser to <i>expect</i> the method, and to throw if not found.
 *
 * @see red.jackf.tomlconfig.reflections.ClassPopulator#toObject(Type, TOMLValue)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Setter {
    String value();
}
