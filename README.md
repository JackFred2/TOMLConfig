# TOMLParser
No-dependency system for parsing .toml files, with utilities for use as a configuration library.

## Floats / Doubles
The project uses the TOML spec's definition of "Float" in it's naming scheme, however  they are effectively Java 
`double`s and should be treated as such.

## Integers / Longs
As above, the project uses the TOML spec's definition of "Integer" in it's naming scheme, however they are effectively 
Java `long`s and should be treated as such.

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
