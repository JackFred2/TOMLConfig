package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.data.TOMLFloat;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class DoubleMapping implements Mapping<Double> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLFloat((Double) object);
    }

    @Override
    public Double toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) {
        return ((TOMLFloat) value).getValue();
    }
}
