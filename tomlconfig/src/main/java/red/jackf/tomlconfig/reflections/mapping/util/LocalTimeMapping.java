package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.data.TOMLDateTime;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;
import java.time.LocalTime;

public class LocalTimeMapping implements Mapping<LocalTime> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        return new TOMLDateTime((LocalTime) object);
    }

    @Override
    public LocalTime toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException {
        return (LocalTime) ((TOMLDateTime) value).getTime();
    }
}
