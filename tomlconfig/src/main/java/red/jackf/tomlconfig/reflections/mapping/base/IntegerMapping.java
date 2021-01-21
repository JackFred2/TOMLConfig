package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLInteger;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class IntegerMapping implements Mapping<Integer> {
    @Override
    public TOMLValue fromObject(Integer object) {
        return new TOMLInteger(object.longValue());
    }

    @Override
    public Integer toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLInteger) value).getValue().intValue();
    }
}
