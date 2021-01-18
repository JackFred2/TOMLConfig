package red.jackf.tomlconfig.parser.data;

import red.jackf.tomlconfig.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TOMLArray implements TOMLValue {
    private boolean sealed = false;
    private final List<TOMLValue> values = new ArrayList<>();

    public void addData(TOMLValue value) throws ParsingException {
        if(sealed) throw new ParsingException("Attempt to add an object to a sealed array.");
        this.values.add(value);
    }

    public TOMLValue getData(int index) {
        return this.values.get(index);
    }

    public int size() {
        return this.values.size();
    }

    @Override
    public void changeTableArraysToArrays() throws ParsingException {
        ListIterator<TOMLValue> iter = values.listIterator();
        while (iter.hasNext()) {
            TOMLValue value = iter.next();
            if (value instanceof TOMLTableArray) {
                iter.set(((TOMLTableArray) value).toArray());
            }
            iter.previous();
            iter.next().changeTableArraysToArrays();
        }
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public void seal() {
        this.sealed = true;
    }
}
