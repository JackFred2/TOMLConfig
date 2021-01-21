package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLInteger;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class LongMapping implements Mapping<Long> {
    @Override
    public TOMLValue fromObject(Long object) {
        return new TOMLInteger(object);
    }

    @Override
    public Long toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLInteger) value).getValue();
    }
}
