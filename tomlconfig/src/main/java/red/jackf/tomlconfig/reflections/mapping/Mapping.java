package red.jackf.tomlconfig.reflections.mapping;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;

import java.lang.reflect.Type;

/**
 * A {@link Mapping} defines a method to map a {@link TOMLValue} to a given class {@link Object}, and vice versa.
 */
public interface Mapping<T> {
    /**
     * Create a new {@link TOMLValue} from a given {@code T} object.
     *
     * @param populator
     * @param object {@code T} object to read data from.
     * @return A {@link TOMLValue} populated with the given object's data.
     */
    TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException;

    /**
     * Create a new {@code T} object from a given {@link TOMLValue} object.
     *
     * @param populator
     * @param value {@link TOMLValue} to read data from
     * @return {@code T} object, parsed from the given {@link TOMLValue}.
     */
    T toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) throws ParsingException;
}
