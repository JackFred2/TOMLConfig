package red.jackf.tomlconfig.parser.data;

public class TOMLBoolean implements TOMLValue {
    private final Boolean value;

    public TOMLBoolean(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TOMLBoolean<" + value + '>';
    }
}
