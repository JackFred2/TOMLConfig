package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.data.TOMLString;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class StringMapping implements Mapping<String> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLString((String) object);
    }

    @Override
    public String toObject(ClassPopulator populator, Type type, TOMLValue value) {
        return ((TOMLString) value).getValue();
    }
}
