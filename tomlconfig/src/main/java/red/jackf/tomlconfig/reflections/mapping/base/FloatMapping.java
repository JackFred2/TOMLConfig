package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLFloat;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class FloatMapping implements Mapping<Float> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLFloat(((Float) object).doubleValue());
    }

    @Override
    public Float toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) {
        return ((TOMLFloat) value).getValue().floatValue();
    }
}
