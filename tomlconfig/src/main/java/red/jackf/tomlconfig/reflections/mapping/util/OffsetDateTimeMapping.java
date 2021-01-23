package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.data.TOMLDateTime;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;

public class OffsetDateTimeMapping implements Mapping<OffsetDateTime> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        return new TOMLDateTime((OffsetDateTime) object);
    }

    @Override
    public OffsetDateTime toObject(ClassPopulator populator, TOMLValue value, Type typeInfo) throws ParsingException {
        return (OffsetDateTime) ((TOMLDateTime) value).getTime();
    }
}
