package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.data.TOMLString;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class CharMapping implements Mapping<Character> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLString(object.toString());
    }

    @Override
    public Character toObject(ClassPopulator populator, Type type, TOMLValue value) {
        return ((TOMLString) value).getValue().charAt(0);
    }
}
