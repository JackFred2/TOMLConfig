package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLString;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class CharMapping implements Mapping<Character> {
    @Override
    public TOMLValue fromObject(Character object) {
        return new TOMLString(object.toString());
    }

    @Override
    public Character toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLString) value).getValue().charAt(0);
    }
}
