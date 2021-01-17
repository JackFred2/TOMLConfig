package red.jackf.tomlconfig.parser.data;

public class TOMLInteger implements TOMLValue {
    private final Integer value;

    public TOMLInteger(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TOMLInteger<" + value + ">";
    }
}
