package red.jackf.tomlconfig.parser;

import red.jackf.tomlconfig.parser.token.KeyJoinToken;
import red.jackf.tomlconfig.parser.token.StringToken;
import red.jackf.tomlconfig.parser.token.Token;

import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TOMLParser {
    // https://toml.io/en/v1.0.0
    public static final TOMLParser INSTANCE = new TOMLParser();

    private static final Pattern KEY_JOIN_PATTERN = Pattern.compile("^[ \t]*\\.");

    private static final Pattern BARE_STRING_PATTERN = Pattern.compile("^[ \t]*(?<contents>[A-Za-z0-9-_]+)");
    private static final Pattern BASIC_STRING_PATTERN = Pattern.compile("^[ \t]*\"(?<contents>(?:[^\\\\\"\n]|\\\\[^\n])*)\"");
    private static final Pattern LITERAL_STRING_PATTERN = Pattern.compile("^[ \t]*'(?<contents>[^'\n]*?)'");
    private static final Pattern BASIC_MULTILINE_STRING_PATTERN = Pattern.compile("^[ \t]*\"\"\"(?:\n)?(?<contents>|(?<quotes>\"{0,2})(?:(?:[^\\\\]|\\\\(?:.|\n))*?(?<!\"\"\"))\\k<quotes>(?<![^\"]\"\"\"))\"\"\"");
    private static final Pattern LITERAL_MULTILINE_STRING_PATTERN = Pattern.compile("^[ \t]*'''(?:\n)?(?<contents>(?<quotes>'{0,2}).*?\\k<quotes>)'''");

    public void parse(String contents) {
        List<Token> tokens = tokenize(contents);
        Deque<Token> tokenStack = new ArrayDeque<>();
    }

    public List<Token> tokenize(String contents) {
        StringReader reader = new StringReader(contents);
        List<Token> tokens = new ArrayList<>();
        while (!reader.ended()) {
            tokens.add(getNextToken(reader, Set.of(StringToken.class, KeyJoinToken.class)));
        }
        System.out.println(tokens);
        return tokens;
    }

    public Token getNextToken(StringReader contents, Set<Class<? extends Token>> expects) {
        String toRead = contents.getRemaining();
        Matcher matcher;
        Token token;
        if ((matcher = KEY_JOIN_PATTERN.matcher(toRead)).find()) {
            if (expects.contains(KeyJoinToken.class))
                token = new KeyJoinToken();
            else
                throw new IllegalArgumentException("Found KeyJoinToken when expected" + expects);
        } else if ((matcher = BARE_STRING_PATTERN.matcher(toRead)).find()) {
            if (expects.contains(StringToken.class))
                token = new StringToken(matcher.group("contents"), StringToken.Type.BARE);
            else
                throw new IllegalArgumentException("Found BARE StringToken when expected " + expects);
        } else if ((matcher = BASIC_MULTILINE_STRING_PATTERN.matcher(toRead)).find()) {
            if (expects.contains(StringToken.class))
                token = new StringToken(matcher.group("contents"), StringToken.Type.BASIC_MULTILINE);
            else
                throw new IllegalArgumentException("Found BASIC_MULTILINE StringToken when expected " + expects);
        } else if ((matcher = LITERAL_MULTILINE_STRING_PATTERN.matcher(toRead)).find()) {
            if (expects.contains(StringToken.class))
                token = new StringToken(matcher.group("contents"), StringToken.Type.LITERAL_MULTILINE);
            else
                throw new IllegalArgumentException("Found LITERAL_MULTILINE StringToken when expected " + expects);
        } else if ((matcher = BASIC_STRING_PATTERN.matcher(toRead)).find()) {
            if (expects.contains(StringToken.class))
                token = new StringToken(matcher.group("contents"), StringToken.Type.BASIC);
            else
                throw new IllegalArgumentException("Found BASIC StringToken when expected " + expects);
        } else if ((matcher = LITERAL_STRING_PATTERN.matcher(toRead)).find()) {
            if (expects.contains(StringToken.class))
                token = new StringToken(matcher.group("contents"), StringToken.Type.LITERAL);
            else
                throw new IllegalArgumentException("Found LITERAL StringToken when expected " + expects);
        } else {
            throw new IllegalArgumentException("Unknown token");
        }
        contents.addIndex(matcher.end());
        return token;
    }

    private static class StringReader {
        private final String source;
        private int index = 0;

        public StringReader(String source) {
            this.source = source;
        }

        public void addIndex(int offset) {
            index = Math.min(index + offset, source.length());
        }

        public int getIndex() {
            return this.index;
        }

        public boolean ended() {
            return index == source.length();
        }

        public String getRemaining() {
            return source.substring(index);
        }
    }
}
