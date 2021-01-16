package red.jackf.tomlconfig.parser.token;

public class IntegerToken extends Token {
    private final int value;

    public IntegerToken(int index, String text, int radix) {
        super(index);
        this.value = Integer.valueOf(text, radix);
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
}
