package red.jackf.tomlconfig.parser;

import red.jackf.tomlconfig.parser.token.*;

import java.util.Map;
import java.util.Set;

public enum ParserState {
    BASE,
    KEY,
    KEY_JOINER,
    TABLE_BASE,
    TABLE_KEY,
    TABLE_KEY_JOINER,
    TABLE_END,
    ASSIGNING,
    END_OF_LINE;

    /**
     * A map of token types, to the state after matching that token.
     */
    private Map<Class<? extends Token>, ParserState> optionMap;

    static {
        BASE.optionMap = Map.of(
            BareStringToken.class, KEY,
            StringToken.class, KEY,
            TableBeginToken.class, TABLE_BASE,
            EndOfLineToken.class, BASE,
            CommentToken.class, END_OF_LINE);

        KEY.optionMap = Map.of(
            AssignmentToken.class, ASSIGNING,
            KeyJoinToken.class, KEY_JOINER);

        KEY_JOINER.optionMap = Map.of(
            BareStringToken.class, KEY,
            StringToken.class, KEY);

        TABLE_BASE.optionMap = Map.of(BareStringToken.class, TABLE_KEY);

        TABLE_KEY.optionMap = Map.of(
            KeyJoinToken.class, TABLE_KEY_JOINER,
            TableEndToken.class, TABLE_END);

        TABLE_KEY_JOINER.optionMap = Map.of(
            BareStringToken.class, TABLE_KEY,
            StringToken.class, TABLE_KEY);

        TABLE_END.optionMap = Map.of(
            CommentToken.class, END_OF_LINE,
            EndOfLineToken.class, BASE);

        END_OF_LINE.optionMap = Map.of(EndOfLineToken.class, BASE);

        ASSIGNING.optionMap = Map.of(
            IntegerToken.class, END_OF_LINE,
            FloatToken.class, END_OF_LINE,
            StringToken.class, END_OF_LINE,
            MultilineStringToken.class, END_OF_LINE,
            BooleanToken.class, END_OF_LINE
        );
    }

    public static ParserState initial() {
        return BASE;
    }

    public Set<Class<? extends Token>> getExpects() {
        return optionMap.keySet();
    }

    public ParserState getAfter(Class<? extends Token> selected) {
        if (!optionMap.containsKey(selected)) throw new IllegalStateException("Invalid option for state " + this + " selected.");
        return optionMap.get(selected);
    }
}
