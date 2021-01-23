package red.jackf.tomlconfig.exceptions;

public class ParsingException extends Exception {
    public ParsingException(String cause) {
        super(cause);
    }

    public ParsingException(Exception e) {
        super(e);
    }
}
