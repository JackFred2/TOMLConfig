package red.jackf.tomlconfig.parser.data;

import org.jetbrains.annotations.Nullable;
import red.jackf.tomlconfig.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TOMLTableArray extends TOMLTable {
    private final List<TOMLTable> data = new ArrayList<>();

    public TOMLTableArray() {

    }

    public void increaseIndex() {
        data.add(new TOMLTable());
    }

    @Override
    public void addData(String key, TOMLValue value) throws ParsingException {
        data.get(data.size() - 1).addData(key, value);
    }

    @Override
    public @Nullable TOMLValue getData(String route) {
        return data.get(data.size() - 1).getData(route);
    }

    @Override
    public void seal(Sealed newVal) {
        return;
    }

    @Override
    public String toString() {
        return "TOMLTableArray<" + data + '>';
    }
}
