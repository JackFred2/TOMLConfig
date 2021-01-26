package red.jackf.tomlconfig.data;

public class TOMLBoolean extends TOMLValue {
    private final Boolean value;

    public TOMLBoolean(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
