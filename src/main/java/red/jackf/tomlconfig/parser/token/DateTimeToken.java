package red.jackf.tomlconfig.parser.token;

import java.time.temporal.Temporal;

public class DateTimeToken extends Token {
    private final Temporal time;

    public DateTimeToken(int index, Temporal time) {
        super(index);
        this.time = time;
    }

    public Temporal getTime() {
        return time;
    }
}
