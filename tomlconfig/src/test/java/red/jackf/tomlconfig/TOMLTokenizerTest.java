package red.jackf.tomlconfig;

import org.junit.Test;
import red.jackf.tomlconfig.exceptions.TokenizationException;
import red.jackf.tomlconfig.parser.TOMLParser;
import red.jackf.tomlconfig.parser.TOMLTokenizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TOMLTokenizerTest {
    @Test
    public void testFile() throws TokenizationException {
        String file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/testfile2.toml"))).lines().collect(Collectors.joining("\n"));
        TOMLParser.INSTANCE.parse(file);
    }
}
