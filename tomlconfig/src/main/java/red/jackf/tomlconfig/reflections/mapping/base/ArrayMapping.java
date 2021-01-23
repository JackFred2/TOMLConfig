package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.data.TOMLArray;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

public class ArrayMapping implements Mapping<Object> {

    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        TOMLArray array = new TOMLArray();
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            array.addData(populator.fromObject(Array.get(object, i)));
        }
        return array;
    }

    @Override
    public Object toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) throws ParsingException {
        Class<?> clazz = ((Class<?>) typeInfo);
        TOMLArray array = ((TOMLArray) value);
        Object toReturn = Array.newInstance(clazz.getComponentType(), array.size());
        for (int i = 0; i < array.size(); i++) {
            Array.set(toReturn, i, populator.toObject(clazz.getComponentType(), array.getData(i)));
        }
        return toReturn;
    }
}
