package red.jackf.tomlconfig.parser;

import red.jackf.tomlconfig.parser.token.*;

import java.util.*;
import java.util.regex.Matcher;

import static red.jackf.tomlconfig.parser.Patterns.*;

public class TOMLParser {
    // https://toml.io/en/v1.0.0
    public static final TOMLParser INSTANCE = new TOMLParser();

    public void parse(String contents) {
        List<Token> tokens = tokenize(contents);
        Deque<Token> tokenStack = new ArrayDeque<>();
    }

    public List<Token> tokenize(String contents) {
        StringReader reader = new StringReader(contents);
        List<Token> tokens = new ArrayList<>();
        ParserState state = ParserState.initial();
        while (!reader.ended()) {
            Token token = getNextToken(reader, state.getExpects());
            state = state.getAfter(token.getClass());
            tokens.add(token);
        }
        return tokens;
    }

    public Token getNextToken(StringReader contents, Set<Class<? extends Token>> expects) {
        String toRead = contents.getRemaining();
        Matcher matcher;
        Token token;

        // remove preceding whitespace
        matcher = WHITESPACE.matcher(toRead);
        if (matcher.find()) {
            contents.addIndex(matcher.end());
            toRead = contents.getRemaining();
        }

        if ((matcher = COMMENT.matcher(toRead)).find()) {
            token = new CommentToken();

        } else if ((matcher = END_OF_LINE.matcher(toRead)).find()) {
            token = new EndOfLineToken();

        } else if (expects.contains(AssignmentToken.class) && (matcher = ASSIGNMENT.matcher(toRead)).find()) {
            token = new AssignmentToken();

        } else if (expects.contains(KeyJoinToken.class) && (matcher = KEY_JOIN.matcher(toRead)).find()) {
            token = new KeyJoinToken();

        } else if (expects.contains(TableBeginToken.class) && (matcher = TABLE_BEGIN.matcher(toRead)).find()) {
            token = new TableBeginToken();
        } else if (expects.contains(TableEndToken.class) && (matcher = TABLE_END.matcher(toRead)).find()) {
            token = new TableEndToken();

        } else if (expects.contains(BooleanToken.class) && (matcher = BOOLEAN.matcher(toRead)).find()) {
            token = new BooleanToken(matcher.group());

        } else if (expects.contains(FloatToken.class) && (matcher = SPECIAL_FLOAT.matcher(toRead)).find()) {
            token = new FloatToken(matcher.group(1), true);
        } else if (expects.contains(FloatToken.class) && (matcher = STANDARD_FLOAT.matcher(toRead)).find()) {
            token = new FloatToken(matcher.group(1), false);

        } else if (expects.contains(IntegerToken.class) && (matcher = DEC_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(matcher.group(1), 10);
        } else if (expects.contains(IntegerToken.class) && (matcher = HEX_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(matcher.group(1), 16);
        } else if (expects.contains(IntegerToken.class) && (matcher = OCT_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(matcher.group(1), 8);
        } else if (expects.contains(IntegerToken.class) && (matcher = BIN_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(matcher.group(1), 2);

        } else if (expects.contains(BareStringToken.class) && (matcher = BARE_STRING.matcher(toRead)).find()) { // Bare Strings (Keys)
            token = new BareStringToken(matcher.group("contents"));

        } else if (expects.contains(MultilineStringToken.class) && (matcher = BASIC_MULTILINE_STRING.matcher(toRead)).find()) { // Multi Line Strings
            token = new MultilineStringToken(matcher.group("contents"), MultilineStringToken.Type.BASIC);
        } else if (expects.contains(MultilineStringToken.class) && (matcher = LITERAL_MULTILINE_STRING.matcher(toRead)).find()) {
            token = new MultilineStringToken(matcher.group("contents"), MultilineStringToken.Type.LITERAL);

        } else if (expects.contains(StringToken.class) && (matcher = BASIC_STRING.matcher(toRead)).find()) { // Single Line Strings
            token = new StringToken(matcher.group("contents"), StringToken.Type.BASIC);
        } else if (expects.contains(StringToken.class) && (matcher = LITERAL_STRING.matcher(toRead)).find()) {
            token = new StringToken(matcher.group("contents"), StringToken.Type.LITERAL);

        } else {
            throw new IllegalArgumentException("Unknown token (expected one of " + expects + " in " + toRead.substring(0, 15) + ")");
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

        public boolean ended() {
            return index >= source.length();
        }

        public String getRemaining() {
            return source.substring(index);
        }
    }
}
