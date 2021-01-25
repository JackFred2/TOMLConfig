package red.jackf.tomlconfig;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class TOMLConfigTester {
    @Test
    public void testSerializationAndDeserialization() {
        TOMLConfig CONFIG = TOMLConfig.builder().withIndentationStep(4).build();
        ExampleConfig base = new ExampleConfig();
        CONFIG.writeConfig(base);
        assertTrue(Files.exists(FileSystems.getDefault().getPath("ExampleConfig.toml")));
        ExampleConfig loaded = CONFIG.readConfig(ExampleConfig.class);
        assertEquals(loaded, base);
        assertNotSame(loaded, base);
        assertTrue(loaded.setterCalled);
    }

    @After
    public void deleteTestFiles() throws IOException {
        Files.delete(FileSystems.getDefault().getPath("ExampleConfig.toml"));
    }
}
