package red.jackf.tomlconfig.writer;

import red.jackf.tomlconfig.data.*;
import red.jackf.tomlconfig.settings.KeySortMode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TOMLWriter is responsible for converting a {@link TOMLValue} object to a string representation. This class is <i>not
 * thread-safe</i>.
 */
public class TOMLWriter {
    private final int indentStep;
    private final KeySortMode keySortMode;
    private final int maxLineWidth;

    private StringBuilder builder = new StringBuilder();
    private final Stack<String> tableStack = new Stack<>();

    private int indentLevel = 0;
    private int normalArrayDepth = 0;
    private int inlineTableDepth = 0;
    private int newlineCount = 0;

    public TOMLWriter(int indentStep, int maxLineWidth, KeySortMode keySortMode) {
        this.maxLineWidth = maxLineWidth;
        this.indentStep = indentStep;
        this.keySortMode = keySortMode;
    }

    private void clear() {
        this.builder = new StringBuilder();
        this.tableStack.clear();
    }

    private void indent() {
        indentLevel += indentStep;
    }

    private void deIndent() {
        if (indentLevel == 0) throw new IllegalStateException("Attempted to get negative indentation");
        indentLevel -= indentStep;
    }

    private void append(String str) {
        newlineCount = 0;
        builder.append(str);
    }

    private void append(char c) {
        newlineCount = 0;
        builder.append(c);
    }

    /**
     * Serialize a TOMLValue using this TOMLWriter's settings.
     * @param tomlValue {@link TOMLValue} to serialize.
     * @return String representation of the TOML passed.
     */
    public String writeToString(TOMLValue tomlValue) {
        clear();
        writeToString(tomlValue, false);
        return get();
    }

    private void writeToString(TOMLValue toml, boolean skipTitle) {
        if (toml instanceof TOMLDateTime) {
            TOMLDateTime dateTime = (TOMLDateTime) toml;
            append(dateTime.getTime().toString());
        } else if (toml instanceof TOMLFloat) {
            TOMLFloat tomlFloat = (TOMLFloat) toml;
            Double value = tomlFloat.getValue();
            if (value.isInfinite()) {
                if (value == Double.NEGATIVE_INFINITY) {
                    append("-inf");
                } else {
                    append("inf");
                }
            } else if (value.isNaN()) {
                append("nan");
            } else {
                append(value.toString());
            }
        } else if (toml instanceof TOMLInteger) {
            append(((TOMLInteger) toml).getValue().toString());
        } else if (toml instanceof TOMLBoolean) {
            append(((TOMLBoolean) toml).getValue() ? "true" : "false");
        } else if (toml instanceof TOMLString) {
            String str = toml.toString();
            if (str.charAt(0) != '"' && str.charAt(0) != '\'') str = "\"" + str + "\"";
            append(str);
        } else if (toml instanceof TOMLArray) {
            TOMLArray array = (TOMLArray) toml;
            if (array.onlyTables() && normalArrayDepth == 0) {

                for (int i = 0; i < array.size(); i++) {
                    newLine();
                    if (i == 0) doComment(array);
                    append("[[");
                    printTableNameStack();
                    append("]]");
                    indent();
                    newLine();
                    deIndent();
                    writeToString(array.getData(i), true);
                    if (i < array.size() - 1) newLine();
                }
            } else {
                append('[');
                if (array.size() > 0) {
                    normalArrayDepth++;
                    indent();
                    if (inlineTableDepth == 0) {
                        newLine();
                    } else {
                        append(' ');
                    }
                    for (int i = 0; i < array.size(); i++) {
                        writeToString(array.getData(i), false);
                        if (i == array.size() - 1) deIndent();
                        else append(',');
                        if (inlineTableDepth == 0) {
                            newLine();
                        } else {
                            append(' ');
                        }
                    }
                    normalArrayDepth--;
                }
                append(']');
            }
        } else if (toml instanceof TOMLTable) {
            TOMLTable table = (TOMLTable) toml;
            Map<String, TOMLValue> data = table.getAllData();
            List<String> keys = keySortMode == KeySortMode.DECLARATION_ORDER ? new ArrayList<>(data.keySet()) : data.keySet().stream().sorted().collect(Collectors.toList());
            if (normalArrayDepth == 0) {
                boolean root = tableStack.empty();

                // append name
                if (!root) {
                    if (!skipTitle) {
                        append('[');
                        printTableNameStack();
                        append(']');
                    }
                    indent();
                    if (!skipTitle) newLine();
                }

                List<String> tables = new ArrayList<>();
                List<String> tableArrays = new ArrayList<>();

                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    TOMLValue value = data.get(key);
                    if (value instanceof TOMLTable) tables.add(key);
                    else if ((value instanceof TOMLArray) && (((TOMLArray) value).onlyTables())) tableArrays.add(key);
                    else {
                        doComment(value);
                        if (value instanceof TOMLArray) tableStack.push(key);
                        addKey(key);
                        writeToString(value, false);
                        if (value instanceof TOMLArray) tableStack.pop();
                        if (i < keys.size() - 1 || tables.size() > 0)
                            newLine();
                    }
                }

                for (String key : tables) {
                    TOMLValue value = data.get(key);
                    newLine();
                    doComment(value);
                    tableStack.push(TOMLString.toTOMLString(key));
                    writeToString(value, false);
                    tableStack.pop();
                }

                for (String key : tableArrays) {
                    TOMLArray value = (TOMLArray) data.get(key);
                    if (value.size() != 0) newLine();
                    tableStack.push(TOMLString.toTOMLString(key));
                    writeToString(value, false);
                    tableStack.pop();
                }

                if (!root) {
                    deIndent();
                }
            } else {
                append("{ ");
                inlineTableDepth++;

                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    addKey(key);
                    writeToString(data.get(key), false);
                    if (i < keys.size() - 1) builder.append(", ");
                }
                inlineTableDepth--;
                append(" }");
            }
        }
    }

    private String get() {
        return builder.toString();
    }

    private void doComment(TOMLValue value) {
        if (value.getComment() != null) {
            List<String> lines = new ArrayList<>();
            List<List<String>> words = new ArrayList<>();
            for (String s : value.getComment().split("\n")) {
                words.add(Arrays.asList(s.split(" ")));
            }
            StringBuilder current = new StringBuilder();
            for (List<String> wordList : words) {
                for (int i = 0; i < wordList.size(); i++) {
                    String word = wordList.get(i);
                    if (current.length() == 0 || current.length() + 1 + word.length() <= maxLineWidth - 2 - indentLevel) { // break word onto new line
                        if (current.length() == 0) current = new StringBuilder(word);
                        else current.append(" ").append(word);
                    } else {
                        lines.add(current.toString());
                        current = new StringBuilder();
                        current.append(word);
                    }
                }
                lines.add(current.toString());
                current = new StringBuilder();
            }
            for (String line : lines) {
                append("# ");
                append(line);
                newLine();
            }
        }
    }

    /**
     * Writes the current table name stack in the form a.b.c
     */
    private void printTableNameStack() {
        append(TOMLString.toTOMLString(tableStack.get(0)));
        for (int i = 1; i < tableStack.size(); i++) {
            append('.');
            append(TOMLString.toTOMLString(tableStack.get(i)));
        }
    }

    private void addKey(String name) {
        append(TOMLString.toTOMLString(name));
        append(" = ");
    }

    private void newLine() {
        if (newlineCount <= 1) {
            append('\n');
            for (int i = 0; i < indentLevel; i++) append(' ');
            newlineCount++;
        }
    }
}
