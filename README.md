# TOMLParser

No-dependency system for parsing .toml files, with utilities for use as a configuration library. Follows the 
[TOML Spec](https://toml.io/en/v1.0.0). Licensed under the LGPL-3.0

## Dates, Times, and DateTimes
TOMLConfig supports Offset Date-Times, Local Date-Times, Local Dates, and Local Times, and maps them to java `OffsetDateTime`, `LocalDateTime`, `LocalDate` and `LocalTime` objects from the `java.time` package.

Second fractions are supported up to the nanosecond (9 digits).

## Quickstart
### Project Setup
Replace `%VERSION%` with the latest: 

[ ![Download](https://api.bintray.com/packages/jackfred/TOMLConfig/TOMLConfig/images/download.svg) ](https://bintray.com/jackfred/TOMLConfig/TOMLConfig/_latestVersion)

#### Gradle
```groovy
dependencies {
    implementation 'red.jackf:TOMLConfig:%VERSION%'
}
```

#### Maven
```xml
<dependency>
  <groupId>red.jackf</groupId>
  <artifactId>TOMLConfig</artifactId>
  <version>%VERSION%</version>
  <type>pom</type>
</dependency>
```

### Creating a configuration file

A configuration file is a standard Java class that implements `red.jackf.tomlconfig.annotations.Config`:
```java
import red.jackf.tomlconfig.annotations.Config;

// ...

public class ExampleConfig implements Config {
    public boolean doThisThing = false;
    public int thatThingIndex = 42;
    // ... and so on
}
```

### Saving and loading the file

Saving and loading is done by creating a `TOMLConfig` object, and calling `TOMLConfig#readConfig()`:
```java
import red.jackf.tomlconfig.TOMLConfig;

// ...

public class MyApp {
    private static final TOMLConfig TOML_CONFIG = TOMLConfig.builder().build(); // or TOMLConfig.get();
    private static final ExampleConfig CONFIG = TOML_CONFIG.readConfig(ExampleConfig.class);
    
    public static void main(String[] args) {
        if (CONFIG.doThisThing == true) {
            // ...
        }
    }
}
```

This creates a default configuration file in the program's working directory if none is found.

## Further Configuration

### Saving manually

You can save your configuration during runtime using `TOMLCONFIG#writeConfig()`, such as if it was updated by a GUI or
during startup auto-detection:
```java
public class MyApp {
    private static final TOMLConfig TOML_CONFIG = TOMLConfig.get();
    private static final ExampleConfig CONFIG = TOML_CONFIG.readConfig(ExampleConfig.class);
    
    // ...

    public void updateSettings() {
        CONFIG.thatThingIndex = 435;
        TOML_CONFIG.writeConfig(CONFIG);
    }
}
```

### Config location

You can change both the file name and location by passing a `Path` as a second argument to `readConfig()` and `writeConfig()`:

```java
// Loading the config from ./config/myAppConfig.toml
public class MyApp {
    private static final Path CONFIG_PATH = Paths.get("config", "myAppConfig.toml");
        
    private static final TOMLConfig TOML_CONFIG = TOMLConfig.get();
    private static final ExampleConfig CONFIG = TOML_CONFIG.readConfig(ExampleConfig.class, CONFIG_PATH);
    
    // ...

    public void updateSettings() {
        CONFIG.thatThingIndex = 435;
        TOML_CONFIG.writeConfig(CONFIG, CONFIG_PATH);
    }
}
```

It is also possible to read a config directory from a String by passing a `String` as the second argument, or to write to a Java `Writer` object:

```java
public class MyApp {
    private static final TOMLConfig TOML_CONFIG = TOMLConfig.get();

    // ...

    public ExampleConfig loadConfigFromNetwork() {
        String toml = ExampleNetworkHandler.readString();
        return TOML_CONFIG.readConfig(ExampleConfig.class, toml);
    }

    public void sendConfigToNetwork(ExampleConfig CONFIG) {
        BufferedWriter networkWriter = ExampleNetworkHandler.getNetworkWriter();
        TOML_CONFIG.writeConfig(CONFIG, networkWriter);
        
        // You can also use this to turn the config object into a String:
        StringWriter stringWriter = new StringWriter();
        TOML_CONFIG.writeConfig(CONFIG, stringWriter);
        String toml = stringWriter.toString();
        System.out.println(toml);
    }
}
```

### Changing your TOMLConfig:

You can configure TOMLConfig by using its builder, and using any of the options available. You can see all of them in [TOMLConfig.java](src/main/java/red/jackf/tomlconfig/TOMLConfig.java):
```java
public class MyApp {
    private static final TOMLConfig TOML_CONFIG = TOMLConfig.builder()
        .withMaxLineLength(120) // Change the maximum line length
        .withIndentationStep(4) // Change the number of spaces per indentation step
        .withKeySortMode(KeySortMode.ALPHABETICAL_ORDER) // Change how keys are ordered
        .build();
    
    // ...
}
```

### Comments

Aside from indentation and line length, you can also a
