package red.jackf.tomlconfig.parser.data;

import java.time.temporal.Temporal;

public class TOMLDateTime implements TOMLValue {
    private final Temporal time;

    public TOMLDateTime(Temporal time) {
        this.time = time;
    }

    public Temporal getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "TOMLDateTime{" +
            "time=" + time +
            '}';
    }
}
