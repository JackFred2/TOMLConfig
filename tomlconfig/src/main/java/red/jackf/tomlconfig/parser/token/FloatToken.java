package red.jackf.tomlconfig.parser.token;

public class FloatToken extends Token {
    private final double value;
    private final String raw;

    public FloatToken(int index, String text, boolean special) {
        super(index);
        raw = text;
        if (special) {
            char first = text.charAt(0);
            if (first == '-') {
                assert text.length() == 4;
                String rest = text.substring(1);
                if (rest.equals("nan")) value = Double.NaN;
                else if (rest.equals("inf")) value = Double.NEGATIVE_INFINITY;
                else throw new IllegalArgumentException("Not a valid special double.");
            } else if (first == '+') {
                assert text.length() == 4;
                String rest = text.substring(1);
                if (rest.equals("nan")) value = Double.NaN;
                else if (rest.equals("inf")) value = Double.POSITIVE_INFINITY;
                else throw new IllegalArgumentException("Not a valid special double.");
            } else {
                assert text.length() == 3;
                if (text.equals("nan")) value = Double.NaN;
                else if (text.equals("inf")) value = Double.POSITIVE_INFINITY;
                else throw new IllegalArgumentException("Not a valid special double.");
            }
        } else value = Double.parseDouble(text.replace("_", ""));
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatToken{" +
            "value=" + value +
            '}';
    }

    public String getRaw() {
        return raw;
    }
}
