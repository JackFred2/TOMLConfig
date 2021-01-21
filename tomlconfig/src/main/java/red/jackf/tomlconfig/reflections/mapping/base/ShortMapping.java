package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLInteger;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class ShortMapping implements Mapping<Short> {
    @Override
    public TOMLValue fromObject(Short object) {
        return new TOMLInteger(object.longValue());
    }

    @Override
    public Short toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLInteger) value).getValue().shortValue();
    }
}
