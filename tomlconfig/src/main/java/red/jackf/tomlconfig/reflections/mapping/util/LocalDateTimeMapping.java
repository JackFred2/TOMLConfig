package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.data.TOMLDateTime;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeMapping implements Mapping<LocalDateTime> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        return new TOMLDateTime((LocalDateTime) object);
    }

    @Override
    public LocalDateTime toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException {
        return (LocalDateTime) ((TOMLDateTime) value).getTime();
    }
}
