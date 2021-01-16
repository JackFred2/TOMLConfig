package red.jackf.tomlconfig.parser;

import java.util.regex.Pattern;

public abstract class Patterns {
    public static final Pattern WHITESPACE = Pattern.compile("^[\t ]*");

    public static final Pattern COMMENT = Pattern.compile("^#(.*)(?=\\n|$)");
    public static final Pattern END_OF_LINE = Pattern.compile("^(?:\\n|$)");

    public static final Pattern ASSIGNMENT = Pattern.compile("^=");
    public static final Pattern KEY_JOIN = Pattern.compile("^\\.");

    public static final Pattern DOUBLE_LEFT_BRACKET = Pattern.compile("^\\[\\[");
    public static final Pattern DOUBLE_RIGHT_BRACKET = Pattern.compile("^]]");

    public static final Pattern LEFT_BRACKET = Pattern.compile("^\\[");
    public static final Pattern RIGHT_BRACKET = Pattern.compile("^]");

    public static final Pattern SEPARATOR = Pattern.compile("^,");

    public static final Pattern INLINE_TABLE_BEGIN = Pattern.compile("^\\{");
    public static final Pattern INLINE_TABLE_END = Pattern.compile("^}");

    public static final Pattern BOOLEAN = Pattern.compile("^(true|false)");

    public static final Pattern SPECIAL_FLOAT = Pattern.compile("^([+-]?(?:inf|nan))");
    public static final Pattern STANDARD_FLOAT = Pattern.compile("^([+-]?(?!_)(?:[1-9][0-9_]*|0)(?<!_)(?:[eE][+-]?(?!_)[0-9_]+(?!_)|\\.(?!_)[0-9_]+(?!_)(?:[eE][+-]?(?!_)[0-9_]+(?<!_))?))");

    public static final Pattern DEC_INTEGER = Pattern.compile("^((?:[+-])?(?:[1-9][0-9_]*|0)(?<!_))");
    public static final Pattern HEX_INTEGER = Pattern.compile("^(0[xX](?=[0-9a-fA-F])[0-9a-fA-F_]+(?<!_))");
    public static final Pattern OCT_INTEGER = Pattern.compile("^(0[oO](?=[0-7])[0-7_]+(?<!_))");
    public static final Pattern BIN_INTEGER = Pattern.compile("^(0[bB](?=[01])[01_]+(?<!_))");

    public static final Pattern BARE_STRING = Pattern.compile("^(?<contents>[A-Za-z0-9-_]+)");
    public static final Pattern BASIC_STRING = Pattern.compile("^\"(?<contents>(?:[^\\\\\"\\n]|\\\\[^\\n])*)\"");
    public static final Pattern LITERAL_STRING = Pattern.compile("^'(?<contents>[^'\\n]*?)'");
    public static final Pattern BASIC_MULTILINE_STRING = Pattern.compile("^\"\"\"(?:\\n)?(?<contents>|(?<quotes>\"{0,2})(?:(?:[^\\\\]|\\\\(?:.|\\n))*?(?<!\"\"\"))\\k<quotes>(?<![^\"]\"\"\"))\"\"\""); // Does not strip backslash-newline-whitespaces!
    public static final Pattern LITERAL_MULTILINE_STRING = Pattern.compile("^'''(?:\\n)?(?<contents>(?<quotes>'{0,2}).*?\\k<quotes>)'''");
}
