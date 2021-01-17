# TOMLParser
No-dependency system for parsing .toml files, with utilities for use as a configuration library.

## Floats
The project uses the TOML spec's definition of "Float" in it's naming scheme, however  they are effectively Java `double`s and should be treated as such.

## Feature checklist
- [x] Basic TOML parsing
    - [x] Strings
        - [x] Bare
        - [x] Basic
        - [x] Multiline
    - [x] Integers
    - [x] Floats
    - [ ] Dates
    - [x] Tables
    - [x] Inline Tables
    - [x] Arrays
    - [x] Table Arrays
