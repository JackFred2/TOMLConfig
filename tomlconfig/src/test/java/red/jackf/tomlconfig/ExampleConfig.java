package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;

import java.util.Arrays;

@Config.Data(name = "testConfig")
public class ExampleConfig implements Config {
    public Boolean enabled = true;

    public int[][][] values = {
        {
            {
                1
            },
            {
                2, 3
            }
        },
        {
            {
                4, 5, 6, 7
            }
        },
        {
            {
                0
            }
        }
    };

    @Override
    public String toString() {
        return "ExampleConfig{" +
            "enabled=" + enabled +
            ", values=" + Arrays.deepToString(values) +
            '}';
    }
}
