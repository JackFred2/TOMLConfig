package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLFloat;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class FloatMapping implements Mapping<Float> {
    @Override
    public TOMLValue fromObject(Float object) {
        return new TOMLFloat(object.doubleValue());
    }

    @Override
    public Float toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLFloat) value).getValue().floatValue();
    }
}
