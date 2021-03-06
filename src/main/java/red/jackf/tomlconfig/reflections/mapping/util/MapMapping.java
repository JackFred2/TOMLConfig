package red.jackf.tomlconfig.reflections.mapping.util;

import red.jackf.tomlconfig.data.TOMLArray;
import red.jackf.tomlconfig.data.TOMLKey;
import red.jackf.tomlconfig.data.TOMLTable;
import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.reflections.ClassPopulator;
import red.jackf.tomlconfig.reflections.mapping.Mapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapMapping implements Mapping<Map<?, ?>> {
    @Override
    public TOMLValue fromObject(ClassPopulator populator, Object object) throws ParsingException {
        TOMLArray result = new TOMLArray();
        Map<?, ?> map = (Map<?, ?>) object;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            TOMLTable entryTable = new TOMLTable();
            entryTable.addData(new TOMLKey("key"), populator.fromObject(entry.getKey()));
            entryTable.addData(new TOMLKey("value"), populator.fromObject(entry.getValue()));
            result.addData(entryTable);
        }
        return result;
    }

    @Override
    public Map<?, ?> toObject(ClassPopulator populator, Type type, TOMLValue value) throws ParsingException {
        Map<Object, Object> map = new LinkedHashMap<>();
        TOMLArray array = (TOMLArray) value;
        for (int i = 0; i < array.size(); i++) {
            TOMLTable element = (TOMLTable) array.getData(i);
            Object key = populator.toObject(((ParameterizedType) type).getActualTypeArguments()[0], element.getData(new TOMLKey("key")));
            Object mapValue = populator.toObject(((ParameterizedType) type).getActualTypeArguments()[1], element.getData(new TOMLKey("value")));
            map.put(key, mapValue);
        }
        return map;
    }
}
