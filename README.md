# TOMLParser

No-dependency system for parsing .toml files, with utilities for use as a configuration library. Follows the
[TOML Spec](https://toml.io/en/v1.0.0). Licensed under the LGPL-3.0

## Dates, Times, and DateTimes

TOMLConfig supports Offset Date-Times, Local Date-Times, Local Dates, and Local Times, and maps them to java
`OffsetDateTime`, `LocalDateTime`, `LocalDate` and `LocalTime` objects from the `java.time` package.

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

You can change both the file name and location by passing a `Path` as a second argument to `readConfig()` and
`writeConfig()`:

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

It is also possible to read a config directory from a String by passing a `String` as the second argument, or to write
to a Java `Writer` object:

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

You can configure TOMLConfig by using its builder, and using any of the options available. You can see all of them in
[TOMLConfig.java](src/main/java/red/jackf/tomlconfig/TOMLConfig.java):

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

### Config Class Annotations

TOMLConfig provides a set of annotations in `red.jackf.tomlconfig.annotations` to change the behavior or display of the
config files.

#### Comments

You can add comments to class fields with the `@Comment` annotations:

```java
import red.jackf.tomlconfig.annotations.*;

public class ExampleConfig implements Config {
    @Comment("Credentials for ExampleDB server.")
    public String address = "192.168.5.234:5739";
    public String username = "my-app";
    public String password = "super-secure-made-up-password";

    // Can also be repeated!
    @Comment("Easing effect for the GUI animations.")
    @Comment("See https://sole.github.io/tween.js/examples/03_graphs.html for visualisations")
    public Mode easingMode = Mode.CUBIC;

    public enum Mode {
        LINEAR,
        QUADRATIC,
        CUBIC,
        QUARTIC,
        SINE
    }
}
```

Result:

```toml
# Credentials for ExampleDB server.
address = '192.168.5.234:5739'
username = "my-app"
password = "super-secure-made-up-password"
# Easing effect for the GUI animations.
# See https://sole.github.io/tween.js/examples/03_graphs.html for visualisations
easingMode = "CUBIC"
```

#### Custom Setters

TOMLConfig by default searches for a `setFIELDNAME` method in the config class, and sets the field directory if one
cannot be found. This behavior can be changed with the `@Setter` annotation:

```java
public class ExampleConfig implements Config {
    public double value1 = 0.25; // set directly
    public double value2 = 0.5;

    public void setValue2(Double value2) {
        System.out.println("This setter gets called for value2");
        this.value2 = value2;
    }

    @Setter("")
    public double value3 = 0.75;

    public void setValue3(Double value3) {
        System.out.println("This setter does NOT get called");
        this.value3 = value3;
    }

    @Setter("myFancySetter")
    public double value4 = 1.25;

    public void myFancySetter(Double value4) {
        System.out.println("This setter gets called for value4");
        this.value4 = value4;
    }
}
```

### Transient

The `@Transient` keyword causes the field not to get saved or loaded, and behavior is identical to the Java `transient`
keyword:

```java
public class ExampleConfig implements Config {
    public char prefix = '^';

    public transient float percentage = 0.65;

    @Transient
    public float factor = 0.125;

    public char suffix = '%';
}
```

Result:

```toml
prefix = "^"
suffix = "%"
```

## Sub-objects and @Transitive

Stock TOMLConfig supports the following java types:

| Java Class          | Notes |
|   ----------------- | ----- |
| `Integer`/`int`     |
| `Long`/`long`       |
| `Short`/`short`     |
| `Byte`/`byte`       |
| `Float`/`float`     |
| `Double`/`double`   |
| `Boolean`/`boolean` |
| `Character`/`char`  |
| `String`            |
| `Array` (`T[]`)     |
| `Enum`              |
| `OffsetDateTime`    |
| `LocalDateTime`     |
| `LocalDate`         |
| `LocalTime`         |
| `List<T>`           | Deserialization creates an `ArrayList`.
| `Set<T>`            | Deserialization creates a `HashSet`.
| `Map<S,T>`          | Deserialization creates a `HashMap`.

You can define handlers for other types in two ways: by using the `@Transitive` annotation (easier, less flexible), or 
by registering a custom `Mapping` object.

### @Transitive
Using `@Transitive` on a field or a class causes TOMLConfig to treat the object "as is". This can be used to partition
the config within the file, or to serialize more complex objects:

```java
public class ExampleConfig implements Config {
    public ClientOptions client = new ClientOptions();
    public ServerOptions server = new ServerOptions();

    @Transitive
    public static class ClientOptions {
        public boolean renderQuickly = false;
        public int actionKeyBinding = 293;
    }

    @Transitive
    public static class ServerOptions {
        public String welcomeMessage = "Welcome to the server";
        public List<String> adminNames = new ArrayList<>();
        {
            adminNames.add("Jack");
            adminNames.add("Fred");
        }
        public Set<User> allowedUsers = new HashSet<>();
        {
            allowedUsers.add(new User("Jack", 1));
            allowedUsers.add(new User("Fred", 2));
            allowedUsers.add(new User("Alice", 3));
            allowedUsers.add(new User("Bob", 7));
            allowedUsers.add(new User("Charlie", 11));
        }
    }

    @Transitive
    public static class User {
        public String name;
        public int id;

        // @Transitive objects require a zero argument constructor 
        // Practically, you only need to define one if there are any other constructors.
        private User() {}

        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }
}
```
Result:
```toml
[client]
  renderQuickly = false
  actionKeyBinding = 293

[server]
  welcomeMessage = 'Welcome to the server'
  adminNames = [
    "Jack",
    "Fred"
  ]
  
  # Note the different ordering due to the nature of a HashSet - this can be 
  # remedied using a LinkedHashSet.
  [[server.allowedUsers]]
    name = "Fred"
    id = 2
  
  [[server.allowedUsers]]
    name = "Bob"
    id = 7
  
  [[server.allowedUsers]]
    name = "Charlie"
    id = 11
  
  [[server.allowedUsers]]
    name = "Alice"
    id = 3
  
  [[server.allowedUsers]]
    name = "Jack"
    id = 1
```
**Important Note**: Classes marked with `@Transitive` **must** have a zero-argument constructor available. This means
creating one if a constructor with more than 1 argument has already been defined - it can be `private`.

### Creating a custom mapping

**TODO: Better documentation and examples to come - apologies!**

You can create custom mappings from TOML types to Java types and vice versa by implementing 
`red.jackf.tomlconfig.reflections.mapping.Mapping`, and then registering it to your TOMLConfig object using 
`TOMLConfig#register(Class, Mapping)`. For examples,
see [the standard mappings](src/main/java/red/jackf/tomlconfig/reflections/mapping).

The rules for deciding which mapping is as follows:

1. If the passed object is a Java Array (`T[]`), then the built-in ArrayMapping is used. No changing.
2. If there is a direct mapping for an object available, then that mapping is used.
3. If the passed object is an Enum, then the built-in EnumMapping is used. No changing.
4. If there is a mapping for a supertype of the passed object (such as for an `Interface` or `abstract` class, e.g. 
   a `Map` mapping is available for a passed `HashMap` object) then the **first mapping found** is used. Order is 
   undefined.
5. An exception is thrown due to no available mapping.

If a class, or a field holding the class is marked with `@Transitive`, then no mapping is used, and the class is 
serialized key-by-key.


