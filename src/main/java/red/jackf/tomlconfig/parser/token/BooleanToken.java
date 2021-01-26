package red.jackf.tomlconfig.parser.token;

public class BooleanToken extends Token {
    private final boolean value;

    public BooleanToken(int index, String text) {
        super(index);
        if (text.equals("false")) this.value = false;
        else if (text.equals("true")) this.value = true;
        else throw new IllegalArgumentException("Invalid boolean value");
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BooleanToken{" +
            "value=" + value +
            '}';
    }
}
