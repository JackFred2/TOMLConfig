package red.jackf.tomlconfig;

import org.junit.Test;
import red.jackf.tomlconfig.exceptions.ParsingException;
import red.jackf.tomlconfig.exceptions.TokenizationException;
import red.jackf.tomlconfig.parser.TOMLParser;
import red.jackf.tomlconfig.parser.data.TOMLTable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TOMLTokenizerTest {
    @Test
    public void testFile() throws TokenizationException, ParsingException {
        String file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/testfile0.toml"))).lines().collect(Collectors.joining("\n"));
        TOMLTable table = new TOMLParser().parse(file);
        System.out.println(table);
    }
}
