package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLFloat;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class DoubleMapping implements Mapping<Double> {
    @Override
    public TOMLValue fromObject(Double object) {
        return new TOMLFloat(object);
    }

    @Override
    public Double toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLFloat) value).getValue();
    }
}
