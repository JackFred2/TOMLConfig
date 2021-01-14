package red.jackf.tomlconfig.parser.token;

public class StringToken extends Token {
    private final String text;
    private final Type type;

    public StringToken(String text, Type type) {
        this.type = type;

        switch (type) {
            case BASIC_MULTILINE:
                // remove backslashes followed by newlines and whitespace
                this.text = text.replaceAll("(?:^|[^\\\\])(?:\\\\\\\\)*(\\\\\n[\t \n]*)", "");
                break;
            default:
                this.text = text;
        }
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
        BARE,
        BASIC,
        LITERAL,
        BASIC_MULTILINE,
        LITERAL_MULTILINE
    }
}
