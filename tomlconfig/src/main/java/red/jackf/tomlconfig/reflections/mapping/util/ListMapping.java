package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.data.TOMLArray;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializes all objects that implement the {@link List} interface. Deserialization creates an ArrayList object by default.
 */
public class ListMapping implements Mapping<List<?>> {

    /**
     * Object to TOMLValue, returns a TOMLArray with each element of the list serialized.
     */
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object obj) throws ParsingException {
        List<?> list = (List<?>) obj;
        TOMLArray array = new TOMLArray();
        for (Object object : list) {
            array.addData(populator.fromObject(object));
        }
        return array;
    }

    /**
     * TOMLValue to Object, returns an ArrayList which each element deserialized according to {@code type}'s type parameter.
     */
    @Override
    public List<?> toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException {
        List<Object> list = new ArrayList<>();
        int size = ((TOMLArray) value).size();
        for (int i = 0; i < size; i++) {
            list.add(populator.toObject(((ParameterizedType) type).getActualTypeArguments()[0], ((TOMLArray) value).getData(i)));
        }
        return list;
    }
}
