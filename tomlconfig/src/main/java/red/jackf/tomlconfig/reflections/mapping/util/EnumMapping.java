package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.data.TOMLString;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class EnumMapping implements Mapping<Enum<?>> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        return new TOMLString(((Enum<?>) object).name());
    }

    @Override
    public Enum<?> toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException {
        assert ((Class<?>) type).isEnum();
        for (Object constant : ((Class<?>) type).getEnumConstants()) {
            if (((Enum<?>) constant).name().equals(((TOMLString) value).getValue())) return (Enum<?>) constant;
        }
        throw new ParsingException("No enum found in " + type + " that matches " + value);
    }
}
