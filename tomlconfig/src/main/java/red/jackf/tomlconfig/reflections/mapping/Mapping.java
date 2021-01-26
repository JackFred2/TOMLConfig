package red.jackf.tomlconfig.reflections.mapping;

import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.base.StringMapping;
import red.jackf.tomlconfig.reflections.mapping.util.ListMapping;

import java.lang.reflect.Type;

/**
 * A {@link Mapping} defines a method to map a {@link TOMLValue} to a given class {@link Object}, and vice versa.
 *
 * @see ClassPopulator#register(Class, Mapping)
 * @see StringMapping
 * @see ListMapping
 */
public interface Mapping<T> {
    /**
     * Create a new {@link TOMLValue} from a given {@code T} object.
     *
     * @param populator The {@link ClassPopulator} object containing all loaded mappings. Pass this on recursively if
     *                  needed.
     * @param object    {@code T} object to read data from.
     * @return A {@link TOMLValue} populated with the given object's data.
     * @throws ParsingException If the object cannot be serialized for whatever reason.
     */
    TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException;

    /**
     * Create a new {@code T} object from a given {@link TOMLValue} object.
     *
     * @param populator Pass this on recursively if needed.
     * @param value     {@link TOMLValue} to read data from.
     * @param type      Type parameter of the object to deserialize.
     * @return {@code T} object, parsed from the given {@link TOMLValue}.
     * @throws ParsingException If the object cannot be deserialized for whatever reason.
     */
    T toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException;
}
