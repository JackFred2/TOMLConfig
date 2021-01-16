package red.jackf.tomlconfig;

import org.junit.Test;
import red.jackf.tomlconfig.parser.TOMLParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TOMLTokenizerTest {
    @Test
    public void testFile() {
        String file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/testfile1.toml"))).lines().collect(Collectors.joining("\n"));
        TOMLParser.INSTANCE.parse(file);
    }
}
