# TOMLParser
No-dependency system for parsing .toml files, with utilities for use as a configuration library. Follows the 
[TOML Spec](https://toml.io/en/v1.0.0). Licensed under the LGPL-3.0

## Dates, Times, and DateTimes
TOMLConfig supports Offset Date-Times, Local Date-Times, Local Dates, and Local Times, and maps them to java `OffsetDateTime`, `LocalDateTime`, `LocalDate` and `LocalTime` objects from the `java.time` package.

Second fractions are supported up to the nanosecond (9 digits).

## Getting Started
A full online guide is to come; for now see the JavaDoc for [TOMLConfig.java](tomlconfig/src/main/java/red/jackf/tomlconfig/TOMLConfig.java).