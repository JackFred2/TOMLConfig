package red.jackf.tomlconfig.data;

public class TOMLFloat extends TOMLValue {
    private final Double value;

    public TOMLFloat(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
