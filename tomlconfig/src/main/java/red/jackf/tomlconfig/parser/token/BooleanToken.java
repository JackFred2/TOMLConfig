package red.jackf.tomlconfig.parser.token;

public class BooleanToken extends Token {
    private final boolean value;

    public BooleanToken(String text) {
        if (text.equals("false")) this.value = false;
        else if (text.equals("true")) this.value = true;
        else throw new IllegalArgumentException("Invalid boolean value");
    }

    public boolean isValue() {
        return value;
    }
}
