# TOMLParser
No-dependency system for parsing .toml files, with utilities for use as a configuration library. Follows the 
[TOML Spec](https://toml.io/en/v1.0.0).

## Dates, Times, and DateTimes
TOMLConfig supports Offset Date-Times, Local Date-Times, Local Dates, and Local Times, and maps them to java `OffsetDateTime`, `LocalDateTime`, `LocalDate` and `LocalTime` objects from the `java.time` package.

Second fractions are supported up to the nanosecond (9 digits).

## Feature checklist
- [x] Basic TOML parsing
    - [x] Strings
        - [x] Bare
        - [x] Basic
        - [x] Multiline
    - [x] Integers
    - [x] Floats
    - [x] Dates
    - [x] Tables
    - [x] Inline Tables
    - [x] Arrays
    - [x] Table Arrays
- [x] Text -> TOMLTokens
- [x] TOMLTokens -> Object
- [ ] TOMLTokens -> Text
- [x] Object -> TOMLTokens