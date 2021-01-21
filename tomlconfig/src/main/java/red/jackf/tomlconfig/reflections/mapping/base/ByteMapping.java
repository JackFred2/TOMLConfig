package red.jackf.tomlconfig.reflections.mapping.base;

import red.jackf.tomlconfig.parser.data.TOMLInteger;
import red.jackf.tomlconfig.parser.data.TOMLValue;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.Type;

public class ByteMapping implements Mapping<Byte> {
    @Override
    public TOMLValue fromObject(Byte object) {
        return new TOMLInteger(object.longValue());
    }

    @Override
    public Byte toObject(TOMLValue value, Type typeInfo) {
        return ((TOMLInteger) value).getValue().byteValue();
    }
}
