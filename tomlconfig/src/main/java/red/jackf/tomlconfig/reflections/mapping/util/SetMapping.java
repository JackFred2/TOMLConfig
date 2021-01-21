package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.parser.data.TOMLArray;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetMapping implements Mapping<Set<?>> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object obj) throws ParsingException {
        Set<?> set = (Set<?>) obj;
        TOMLArray array = new TOMLArray();
        for (Object object : set) {
            try {
                array.addData(populator.fromObject(object));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    @Override
    public Set<?> toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) throws ParsingException {
        Set<Object> set = new HashSet<>();
        int size = ((TOMLArray) value).size();
        for (int i = 0; i < size; i++) {
            try {
                set.add(populator.toObject(((ParameterizedType) typeInfo).getActualTypeArguments()[0], ((TOMLArray) value).getData(i)));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        return set;
    }
}
