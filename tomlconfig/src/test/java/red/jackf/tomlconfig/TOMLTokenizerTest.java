package red.jackf.tomlconfig;

import org.junit.Assert;
import org.junit.Test;
import red.jackf.tomlconfig.parser.TOMLParser;
import red.jackf.tomlconfig.parser.token.StringToken;
import red.jackf.tomlconfig.parser.token.Token;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TOMLTokenizerTest {
    @Test
    public void testFile() {
        String file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/testfile.toml"))).lines().collect(Collectors.joining("\n"));
        System.out.println(TOMLParser.INSTANCE.tokenize(file));
    }
}
