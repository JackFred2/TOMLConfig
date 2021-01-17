package red.jackf.tomlconfig.parser.data;

import red.jackf.tomlconfig.parser.token.StringToken;
import red.jackf.tomlconfig.parser.token.Token;

public class TOMLString implements TOMLValue {
    private final String value;

    public TOMLString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TOMLString<" + value + '>';
    }
}
