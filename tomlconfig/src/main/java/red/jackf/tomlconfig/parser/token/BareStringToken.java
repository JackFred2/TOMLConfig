package red.jackf.tomlconfig.parser.token;

public class BareStringToken extends Token {
    private final String contents;

    public BareStringToken(String contents) {
        super();
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "BareStringToken{" +
            "contents='" + contents + '\'' +
            '}';
    }
}
