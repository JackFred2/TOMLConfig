package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLString;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class StringMapping implements Mapping<String> {
    @Override
    public TOMLValue fromObject(String object) {
        return new TOMLString(object);
    }

    @Override
    public String toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLString) value).getValue();
    }
}
