package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.parser.data.TOMLArray;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListMapping implements Mapping<List<?>> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object obj) throws ParsingException {
        List<?> list = (List<?>) obj;
        TOMLArray array = new TOMLArray();
        for (Object object : list) {
            try {
                array.addData(populator.fromObject(object));
            } catch (ReflectiveOperationException e) {
                throw new ParsingException(e);
            }
        }
        return array;
    }

    @Override
    public List<?> toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) throws ParsingException {
        List<Object> list = new ArrayList<>();
        int size = ((TOMLArray) value).size();
        for (int i = 0; i < size; i++) {
            try {
                list.add(populator.toObject(((ParameterizedType) typeInfo).getActualTypeArguments()[0], ((TOMLArray) value).getData(i)));
            } catch (ReflectiveOperationException e) {
                throw new ParsingException(e);
            }
        }
        return list;
    }
}
