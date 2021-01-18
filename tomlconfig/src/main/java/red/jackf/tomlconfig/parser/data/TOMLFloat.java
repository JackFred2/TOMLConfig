package red.jackf.tomlconfig.parser.data;

public class TOMLFloat implements TOMLValue {
    private final Double value;

    public TOMLFloat(double value) {
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
