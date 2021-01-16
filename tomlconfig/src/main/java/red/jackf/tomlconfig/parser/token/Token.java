package red.jackf.tomlconfig.parser.token;

public abstract class Token {
    private final int index;

    public Token(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public int getIndex() {
        return index;
    }
}
