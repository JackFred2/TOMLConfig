package red.jackf.tomlconfig.parser.data;

public class TOMLString implements TOMLValue {
    private final String value;

    public TOMLString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
