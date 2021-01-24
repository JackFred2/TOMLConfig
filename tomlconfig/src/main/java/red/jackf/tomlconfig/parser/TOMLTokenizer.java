package red.jackf.tomlconfig.parser;

import red.jackf.tomlconfig.data.TOMLValue;
import red.jackf.tomlconfig.exceptions.TokenizationException;
import red.jackf.tomlconfig.parser.token.*;
import red.jackf.tomlconfig.parser.token.processing.DoubleLeftBracketToken;
import red.jackf.tomlconfig.parser.token.processing.DoubleRightBracketToken;
import red.jackf.tomlconfig.parser.token.processing.LeftBracketToken;
import red.jackf.tomlconfig.parser.token.processing.RightBracketToken;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static red.jackf.tomlconfig.parser.Patterns.*;

public class TOMLTokenizer {
    // https://toml.io/en/v1.0.0

    /**
     * <p>Tokenize a string into a list of {@link Token} objects representing it's TOML value.
     * <p>Ambiguous tokens like {@code [[} or {@code [} will be processed into valid tokens.</p>
     *
     * @param contents String to tokenize.
     * @return A list of tokens representing the TOML file.
     * @throws TokenizationException If there are any errors tokenizing the file such as unexpected values or mismatched
     *                               array brackets.
     */
    protected List<Token> tokenize(String contents) throws TokenizationException {
        StringReader reader = new StringReader(contents);
        List<Token> rawTokens = readTokens(reader);
        return preprocess(rawTokens, reader);
    }

    // Process out the ambiguous tokens
    private List<Token> preprocess(List<Token> tokens, StringReader reader) throws TokenizationException {
        List<Token> processed = new ArrayList<>();
        int arrayCount = 0;

        // fixes some issues related to the start of the file
        Token last = new EndOfLineToken(-1);

        // Remove ambiguous
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            Token next = i < tokens.size() - 1 ? tokens.get(i + 1) : null;

            Token toAdd = token;

            if (toAdd instanceof LeftBracketToken) { // [
                if (processed.size() > 0) {
                    if (last instanceof AssignmentToken || last instanceof SeparatorToken || last instanceof ArrayBeginToken || arrayCount > 0) {
                        toAdd = new ArrayBeginToken(toAdd.getIndex());
                        arrayCount++;
                    } else {
                        toAdd = new TableBeginToken(toAdd.getIndex());
                    }
                } else {
                    toAdd = new TableBeginToken(toAdd.getIndex());
                }
            } else if (toAdd instanceof RightBracketToken) { // ]
                if (arrayCount == 0) {
                    toAdd = new TableEndToken(toAdd.getIndex());
                } else {
                    toAdd = new ArrayEndToken(toAdd.getIndex());
                    arrayCount--;
                    if (arrayCount < 0)
                        throw new TokenizationException("Unmatched end of array at " + reader.getLineAndChar(token.getIndex()));
                }
            } else if (toAdd instanceof DoubleLeftBracketToken) { // [[
                if (last instanceof EndOfLineToken && arrayCount == 0) {
                    toAdd = new TableArrayBeginToken(toAdd.getIndex());
                } else {
                    processed.add(new ArrayBeginToken(toAdd.getIndex()));
                    toAdd = new ArrayBeginToken(toAdd.getIndex() + 1);
                    arrayCount += 2;
                }
            } else if (toAdd instanceof DoubleRightBracketToken) { // ]]
                if (arrayCount == 0) {
                    toAdd = new TableArrayEndToken(toAdd.getIndex());
                } else {
                    processed.add(new ArrayEndToken(toAdd.getIndex()));
                    toAdd = new ArrayEndToken(toAdd.getIndex() + 1);
                    arrayCount -= 2;
                    if (arrayCount < 0)
                        throw new TokenizationException("Unmatched end of array at " + reader.getLineAndChar(token.getIndex()));
                }
            }

            // Correcting token types (boolean, int, float as keys even thought you shouldn't be doing that)
            if (next instanceof AssignmentToken) {
                if (toAdd instanceof IntegerToken) {
                    toAdd = new BareStringToken(toAdd.getIndex(), ((IntegerToken) toAdd).getRaw());
                } else if (toAdd instanceof FloatToken && ((FloatToken) toAdd).getRaw().matches("^[eE0-9-_]*$")) {
                    toAdd = new BareStringToken(toAdd.getIndex(), ((FloatToken) toAdd).getRaw());
                } else if (toAdd instanceof BooleanToken) {
                    toAdd = new BareStringToken(toAdd.getIndex(), ((BooleanToken) token).getValue() ? "true" : "false");
                }
            }

            processed.add(toAdd);

            last = toAdd;
        }

        if (arrayCount > 0) throw new TokenizationException("Mismatched array tokens");

        // remove comments
        processed.removeIf(token -> token instanceof CommentToken);

        return processed;
    }

    // Generate tokens from the file until EOF
    private List<Token> readTokens(StringReader reader) throws TokenizationException {
        List<Token> tokens = new ArrayList<>();
        while (!reader.ended()) {
            Token token = getNextToken(reader);
            tokens.add(token);
        }
        return tokens;
    }

    // Get the next individual token
    private Token getNextToken(StringReader contents) throws TokenizationException {
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
            token = new CommentToken(contents.index);

        } else if ((matcher = END_OF_LINE.matcher(toRead)).find()) {
            token = new EndOfLineToken(contents.index);

        } else if ((matcher = ASSIGNMENT.matcher(toRead)).find()) {
            token = new AssignmentToken(contents.index);

        } else if ((matcher = KEY_JOIN.matcher(toRead)).find()) {
            token = new KeyJoinToken(contents.index);

        } else if ((matcher = DOUBLE_LEFT_BRACKET.matcher(toRead)).find()) { // table array begin or two left brackets (below)
            token = new DoubleLeftBracketToken(contents.index);
        } else if ((matcher = DOUBLE_RIGHT_BRACKET.matcher(toRead)).find()) { // table array end or two left brackets (below)
            token = new DoubleRightBracketToken(contents.index);

        } else if ((matcher = LEFT_BRACKET.matcher(toRead)).find()) { // table name begin or array begin
            token = new LeftBracketToken(contents.index);
        } else if ((matcher = RIGHT_BRACKET.matcher(toRead)).find()) { // table name end or array end
            token = new RightBracketToken(contents.index);

        } else if ((matcher = INLINE_TABLE_BEGIN.matcher(toRead)).find()) {
            token = new InlineTableBeginToken(contents.index);
        } else if ((matcher = INLINE_TABLE_END.matcher(toRead)).find()) {
            token = new InlineTableEndToken(contents.index);

        } else if ((matcher = SEPARATOR.matcher(toRead)).find()) {
            token = new SeparatorToken(contents.index); // array separator or inline table separator

        } else if ((matcher = DATETIME_FULL.matcher(toRead)).find()) { // date
            int year = Integer.parseInt(matcher.group("year"));
            int month = Integer.parseInt(matcher.group("month"));
            int day = Integer.parseInt(matcher.group("day"));
            if (matcher.group("hour") != null) { // date and time
                int hour = Integer.parseInt(matcher.group("hour"));
                int minute = Integer.parseInt(matcher.group("minute"));
                int second = Integer.parseInt(matcher.group("second"));
                int nanos = 0;
                if (matcher.group("millis") != null) {
                    String millisMatch = matcher.group("millis");
                    nanos = Integer.parseInt(millisMatch);
                    for (int i = 0; i < (9 - millisMatch.length()); i++) nanos *= 10;
                }
                String offsetGroup = matcher.group("offset");
                if (offsetGroup != null) { // date, time and offset
                    if (offsetGroup.equals("z"))
                        offsetGroup = "Z"; // java does not support lowercase z for offset, though TOML spec does
                    ZoneOffset offset = ZoneOffset.of(offsetGroup);
                    token = new DateTimeToken(contents.index, OffsetDateTime.of(year, month, day, hour, minute, second, nanos, offset));
                } else {
                    token = new DateTimeToken(contents.index, LocalDateTime.of(year, month, day, hour, minute, second, nanos));
                }
            } else {
                token = new DateTimeToken(contents.index, LocalDate.of(year, month, day));
            }

        } else if ((matcher = LOCAL_TIME.matcher(toRead)).find()) { // time
            int hour = Integer.parseInt(matcher.group("hour"));
            int minute = Integer.parseInt(matcher.group("minute"));
            int second = Integer.parseInt(matcher.group("second"));
            int nanos = 0;
            if (matcher.group("millis") != null) {
                String millisMatch = matcher.group("millis");
                nanos = Integer.parseInt(millisMatch);
                for (int i = 0; i < (9 - millisMatch.length()); i++) nanos *= 10;
            }
            token = new DateTimeToken(contents.index, LocalTime.of(hour, minute, second, nanos));

        } else if ((matcher = BOOLEAN.matcher(toRead)).find()) {
            token = new BooleanToken(contents.index, matcher.group());

        } else if ((matcher = SPECIAL_FLOAT.matcher(toRead)).find()) {
            token = new FloatToken(contents.index, matcher.group(1), true);
        } else if ((matcher = STANDARD_FLOAT.matcher(toRead)).find()) {
            token = new FloatToken(contents.index, matcher.group(1), false);

        } else if ((matcher = DEC_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(contents.index, matcher.group(1), 10);
        } else if ((matcher = HEX_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(contents.index, matcher.group(1), 16);
        } else if ((matcher = OCT_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(contents.index, matcher.group(1), 8);
        } else if ((matcher = BIN_INTEGER.matcher(toRead)).find()) {
            token = new IntegerToken(contents.index, matcher.group(1), 2);

        } else if ((matcher = BARE_STRING.matcher(toRead)).find()) { // Bare Strings (Keys)
            token = new BareStringToken(contents.index, matcher.group("contents"));

        } else if ((matcher = BASIC_MULTILINE_STRING.matcher(toRead)).find()) { // Multi Line Strings
            token = new MultilineStringToken(contents.index, matcher.group("contents"), MultilineStringToken.Type.BASIC);
        } else if ((matcher = LITERAL_MULTILINE_STRING.matcher(toRead)).find()) {
            token = new MultilineStringToken(contents.index, matcher.group("contents"), MultilineStringToken.Type.LITERAL);

        } else if ((matcher = BASIC_STRING.matcher(toRead)).find()) { // Single Line Strings
            token = new StringToken(contents.index, matcher.group("contents"), StringToken.Type.BASIC);
        } else if ((matcher = LITERAL_STRING.matcher(toRead)).find()) {
            token = new StringToken(contents.index, matcher.group("contents"), StringToken.Type.LITERAL);

        } else {
            throw new TokenizationException("Unknown token at " + contents.getLineAndChar(contents.index) + ": '" + toRead.substring(0, 15) + "'");
        }
        contents.addIndex(matcher.end());
        return token;
    }

    public static class StringReader {
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

        public String getLineAndChar(int pos) {
            if (pos == 0) return "L1 C1";
            String trimmed = source.substring(0, pos);
            long line = trimmed.chars().filter(c -> c == '\n').count() + 1;
            long charPos = trimmed.replaceAll("^(?:.|\\n)*\\n", "").length();
            return "L" + line + " C" + charPos;
        }
    }
}
