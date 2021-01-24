package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.data.TOMLArray;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SetMapping implements Mapping<Set<?>> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object obj) throws ParsingException {
        Set<?> set = (Set<?>) obj;
        TOMLArray array = new TOMLArray();
        for (Object object : set) {
            array.addData(populator.fromObject(object));
        }
        return array;
    }

    @Override
    public Set<?> toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException {
        Set<Object> set = new HashSet<>();
        int size = ((TOMLArray) value).size();
        for (int i = 0; i < size; i++) {
            set.add(populator.toObject(((ParameterizedType) type).getActualTypeArguments()[0], ((TOMLArray) value).getData(i)));
        }
        return set;
    }
}
