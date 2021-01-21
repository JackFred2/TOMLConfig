package red.jackf.tomlconfig.parser.token;

public class IntegerToken extends Token {
    private final Long value;
    private final String raw;

    public IntegerToken(Integer index, String raw, Integer radix) {
        super(index);
        this.raw = raw;
        this.value = Long.valueOf(raw.replace("_", ""), radix);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerToken{" +
            "value=" + value +
            '}';
    }

    public String getRaw() {
        return raw;
    }
}
