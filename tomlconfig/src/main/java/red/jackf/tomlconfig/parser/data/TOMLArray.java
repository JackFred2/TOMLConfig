package red.jackf.tomlconfig.parser.data;

import red.jackf.tomlconfig.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;

public class TOMLArray implements TOMLValue {
    private final List<TOMLValue> values = new ArrayList<>();

    public void addData(TOMLValue value) throws ParsingException {
        this.values.add(value);
    }

    public TOMLValue getData(int index) {
        return this.values.get(index);
    }

    public int size() {
        return this.values.size();
    }

    @Override
    public String toString() {
        return "TOMLArray<" + values + '>';
    }
}
