package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.parser.data.TOMLDateTime;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateMapping implements Mapping<LocalDate> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        return new TOMLDateTime((LocalDate) object);
    }

    @Override
    public LocalDate toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) throws ParsingException {
        return (LocalDate) ((TOMLDateTime) value).getTime();
    }
}
