package red.jackf.tomlconfig.parser.token;

public class IntegerToken extends Token {
    private final int value;

    public IntegerToken(String text, int radix) {
        this.value = Integer.valueOf(text, radix);
    }

    public int getValue() {
        return value;
    }
}
