package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLBoolean;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class BooleanMapping implements Mapping<Boolean> {
    @Override
    public TOMLValue fromObject(Boolean object) {
        return new TOMLBoolean(object);
    }

    @Override
    public Boolean toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLBoolean) value).getValue();
    }
}
