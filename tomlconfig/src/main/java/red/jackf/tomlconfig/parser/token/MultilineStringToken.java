package red.jackf.tomlconfig.parser.token;

public class MultilineStringToken extends Token {
    private final String text;
    private final Type type;

    public MultilineStringToken(int index, String text, Type type) {
        super(index);
        this.text = type == Type.BASIC ? text.replaceAll("(?:^|[^\\\\])(?:\\\\\\\\)*(\\\\\n[\t \n]*)", "") : text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MultilineStringToken{" +
            "text='" + text + '\'' +
            ", type=" + type +
            '}';
    }

    public enum Type {
        BASIC,
        LITERAL
    }
}
