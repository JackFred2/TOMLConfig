package red.jackf.tomlconfig;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TOMLConfigTester {
    @Test
    public void testSerializationAndDeserialization() {
        TOMLConfig CONFIG = TOMLConfig.builder().withIndentationStep(4).build();
        ExampleConfig base = new ExampleConfig();
        base.version = 100;
        base.generatedTime = base.generatedTime.minusHours(13);
        base.mode = ExampleConfig.Mode.DELTA;
        base.character = 'd';
        base.double$Val = -4e-12;
        base.testStr = "l\no\nn\ng";
        base.flags = new ExampleConfig.Point[][]{
            new ExampleConfig.Point[]{
                new ExampleConfig.Point(-40, -45)
            }
        };
        base.users.add(new ExampleConfig.User(2, "god"));
        base.client.maxRenderTime = 4;

        CONFIG.writeConfig(base);
        assertTrue(Files.exists(FileSystems.getDefault().getPath("ExampleConfig.toml")));
        ExampleConfig loaded = CONFIG.readConfig(ExampleConfig.class);
        assertEquals(loaded, base);
        assertNotSame(loaded, base);
        assertTrue(loaded.setterCalled);
    }

    @After
    public void deleteTestFiles() throws IOException {
        Path path1 = Paths.get("ExampleConfig.toml");
        Path path2 = Paths.get("TestConfig.toml");
        if (Files.exists(path1)) Files.delete(path1);
        if (Files.exists(path2)) Files.delete(path2);
    }

    @Test
    public void test() {
        TOMLConfig CONFIG = TOMLConfig.builder().withIndentationStep(2).build();
        TestConfig config = CONFIG.readConfig(TestConfig.class);
        CONFIG.writeConfig(config);
        CONFIG.readConfig(TestConfig.class);
    }
}
