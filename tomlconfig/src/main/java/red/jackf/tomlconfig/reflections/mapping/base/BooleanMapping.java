package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.data.TOMLBoolean;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class BooleanMapping implements Mapping<Boolean> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) {
        return new TOMLBoolean((Boolean) object);
    }

    @Override
    public Boolean toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) {
        return ((TOMLBoolean) value).getValue();
    }
}
