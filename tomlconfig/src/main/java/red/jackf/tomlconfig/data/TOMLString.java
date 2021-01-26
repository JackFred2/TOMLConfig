package red.jackf.tomlconfig.data;

import java.util.regex.Pattern;

public class TOMLString extends TOMLValue {
    private final String value;

    public TOMLString(String value) {
        this.value = value;
    }

    public static String toTOMLString(String value) {
        if (Pattern.matches("^[A-Za-z0-9-_]+$", value)) return value;
        if (value.contains("\n")) {
            if (!value.contains("'''")) return "'''" + value + "'''";
            else return "\"\"\"" + value.replace("\"\"\"", "\"\"\\\"") + "\"\"\"";
        }
        if (!value.contains("'")) return "'" + value + "'";
        else return "\"" + value.replace("\"", "\\\"") + "\"";
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return toTOMLString(value);
    }
}
