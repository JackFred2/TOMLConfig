package red.jackf.tomlconfig.parser;

import red.jackf.tomlconfig.exceptions.TokenizationException;
import red.jackf.tomlconfig.parser.token.Token;

import java.util.List;

public class TOMLParser {
    public static final TOMLParser INSTANCE = new TOMLParser();
    private final TOMLTokenizer TOKENIZER = new TOMLTokenizer();

    private TOMLParser() {}

    public void parse(String contents) throws TokenizationException {
        List<Token> tokens = TOKENIZER.tokenize(contents);
        System.out.println(tokens);
    }
}
