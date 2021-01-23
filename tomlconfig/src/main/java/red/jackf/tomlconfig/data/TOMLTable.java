package red.jackf.tomlconfig.data;

import org.jetbrains.annotations.Nullable;
import red.jackf.tomlconfig.exceptions.ParsingException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TOMLTable extends TOMLValue {
    private Sealed sealed = Sealed.NO;
    private final Map<String, TOMLValue> data = new HashMap<>();

    protected void addData(String key, TOMLValue value) throws ParsingException {
        if (data.containsKey(key)) throw new ParsingException("Key '" + key + "' already exists in TOMLTable object.");
        else data.put(key, value);
    }

    public void addData(TOMLKey route, TOMLValue value) throws ParsingException {
        if ((sealed == Sealed.PARTIAL && !(value instanceof TOMLTable)) || sealed == Sealed.FULL)
            throw new ParsingException("Attempt to modify table after sealing");
        TOMLTable current = this;
        for (int i = 0; i < route.getPath().size(); i++) {
            String key = route.getPath().get(i);

            if (i == route.getPath().size() - 1) { // last in list
                current.addData(key, value);
            } else {
                TOMLValue step = current.getData(key);
                if (step == null) {
                    TOMLTable newTable = new TOMLTable();
                    current.addData(key, newTable);
                    current = newTable;
                } else if (step instanceof TOMLTable) {
                    current = (TOMLTable) step;
                } else {
                    throw new ParsingException("Non-table value already exists for key " + key);
                }
            }
        }
    }

    public Map<String, TOMLValue> getAllData() {
        return data;
    }

    @Nullable
    protected TOMLValue getData(String key) {
        return data.get(key);
    }

    @Nullable
    public TOMLValue getData(TOMLKey route) throws ParsingException {
        TOMLTable current = this;
        for (int i = 0; i < route.getPath().size(); i++) {
            String key = route.getPath().get(i);

            if (i == route.getPath().size() - 1) { // last in list
                return current.getData(key);
            } else {
                TOMLValue step = current.getData(key);
                if (step == null) {
                    return null;
                } else if (step instanceof TOMLTable) {
                    current = (TOMLTable) step;
                } else {
                    throw new ParsingException("Attempted to read " + step.getClass().getSimpleName() + " " + key + " as table");
                }
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public void changeTableArraysToArrays() throws ParsingException {
        Set<String> keys = new HashSet<>(this.data.keySet());
        for (String key : keys) {
            if (this.data.get(key) instanceof TOMLTableArray) {
                this.data.put(key, ((TOMLTableArray) this.data.get(key)).toArray());
            }
            this.data.get(key).changeTableArraysToArrays();
        }
    }

    public void seal(Sealed newVal) {
        this.sealed = newVal;
    }

    public enum Sealed {
        NO, // can write any value
        PARTIAL, // can only write tables
        FULL // cannot write anything
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
