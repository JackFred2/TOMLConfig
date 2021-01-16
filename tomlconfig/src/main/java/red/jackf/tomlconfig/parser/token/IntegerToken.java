package red.jackf.tomlconfig.parser.token;

public class IntegerToken extends Token {
    private final int value;
    private final String raw;

    public IntegerToken(int index, String raw, int radix) {
        super(index);
        this.raw = raw;
        this.value = Integer.valueOf(raw.replace("_", ""), radix);
    }

    public int getValue() {
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
