package red.jackf.tomlconfig.parser.data;

public class TOMLInteger implements TOMLValue {
    private final Long value;

    public TOMLInteger(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
