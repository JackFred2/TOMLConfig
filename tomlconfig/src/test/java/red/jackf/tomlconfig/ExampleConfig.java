package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;

import java.util.Arrays;

public class ExampleConfig implements Config {

    @FieldData(setter = "funni")
    public Boolean enabled = true;

    public void funni(Boolean enabled) {
        System.out.println("Set enabled to " + enabled);
        this.enabled = enabled;
    }

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

    @Transitive
    public Client client = new Client();

    public static class Client {
        int setting = 4;

        @Override
        public String toString() {
            return "Client{" +
                "setting=" + setting +
                '}';
        }
    }

    @Override
    public String toString() {
        return "ExampleConfig{" +
            "enabled=" + enabled +
            ", values=" + Arrays.toString(values) +
            ", client=" + client +
            '}';
    }
}
