package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.data.TOMLInteger;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class IntegerMapping implements Mapping<Integer> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLInteger(((Integer) object).longValue());
    }

    @Override
    public Integer toObject(ClassPopulator populator, Type type, TOMLValue value) {
        return ((TOMLInteger) value).getValue().intValue();
    }
}
