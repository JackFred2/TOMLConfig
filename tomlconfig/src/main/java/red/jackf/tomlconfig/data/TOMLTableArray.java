package red.jackf.tomlconfig.data;

import org.jetbrains.annotations.Nullable;
import red.jackf.tomlconfig.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;

/**
 * An array of tables, using the [[tableArrayName]] syntax.
 * This does not appear in the final TOMLTable, and should be migrated to a {@link TOMLArray} during parsing.
 */
public class TOMLTableArray extends TOMLTable {
    private final List<TOMLTable> data = new ArrayList<>();

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
    public void seal(Sealed newVal) {}

    public TOMLArray toArray() throws ParsingException {
        TOMLArray array = new TOMLArray();
        for (TOMLTable datum : this.data) {
            array.addData(datum);
        }
        return array;
    }

    @Override
    public void changeTableArraysToArrays() throws ParsingException {
        throw new ParsingException("changeTableArrayToArray called on TableArray itself!");
    }

    @Override
    public String toString() {
        return "{}" + data.toString();
    }
}
