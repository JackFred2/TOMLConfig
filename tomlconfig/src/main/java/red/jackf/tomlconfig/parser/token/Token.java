package red.jackf.tomlconfig.parser.token;

/**
 * Represents a single element of a TOML array.
 */
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
