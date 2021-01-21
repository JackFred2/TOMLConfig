package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLInteger;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class IntegerMapping implements Mapping<Integer> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLInteger(((Integer) object).longValue());
    }

    @Override
    public Integer toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) {
        return ((TOMLInteger) value).getValue().intValue();
    }
}
