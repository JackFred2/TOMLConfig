package red.jackf.tomlconfig.data;

public class TOMLInteger extends TOMLValue {
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
