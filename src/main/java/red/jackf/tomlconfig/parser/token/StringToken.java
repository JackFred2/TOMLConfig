package red.jackf.tomlconfig.parser.token;

public class StringToken extends Token {
    private final String text;
    private final Type type;

    public StringToken(int index, String text, Type type) {
        super(index);
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "StringToken{" +
            "text='" + text + '\'' +
            ", type=" + type +
            '}';
    }

    public String getText() {
        return text;
    }

    public enum Type {
        BASIC,
        LITERAL
    }
}
