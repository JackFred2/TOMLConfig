package red.jackf.tomlconfig.writer;

import red.jackf.tomlconfig.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * TOMLWriter is responsible for converting a {@link TOMLValue} object to a string representation. This class is <i>not
 * thread-safe</i>.
 */
public class TOMLWriter {
    private final int indentStep;
    private final int maxLineWidth;

    private StringBuilder builder = new StringBuilder();
    private final Stack<String> tableStack = new Stack<>();

    private int indentLevel = 0;
    private int normalArrayDepth = 0;
    private int inlineTableDepth = 0;

    public TOMLWriter(int indentStep, int maxLineWidth) {
        this.maxLineWidth = maxLineWidth;
        this.indentStep = indentStep;
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

    /**
     * Serialize a TOMLValue using this TOMLWriter's settings.
     * @param tomlValue {@link TOMLValue} to serialize.
     */
    public String writeToString(TOMLValue tomlValue) {
        clear();
        writeToString(tomlValue, false);
        return get();
    }

    private void writeToString(TOMLValue toml, boolean skipTitle) {
        if (toml instanceof TOMLDateTime) {
            TOMLDateTime dateTime = (TOMLDateTime) toml;
            builder.append(dateTime.getTime().toString());
        } else if (toml instanceof TOMLFloat) {
            TOMLFloat tomlFloat = (TOMLFloat) toml;
            Double value = tomlFloat.getValue();
            if (value.isInfinite()) {
                if (value == Double.NEGATIVE_INFINITY) {
                    builder.append("-inf");
                } else {
                    builder.append("inf");
                }
            } else if (value.isNaN()) {
                builder.append("nan");
            } else {
                builder.append(value);
            }
        } else if (toml instanceof TOMLInteger) {
            builder.append(((TOMLInteger) toml).getValue().toString());
        } else if (toml instanceof TOMLBoolean) {
            builder.append(((TOMLBoolean) toml).getValue() ? "true" : "false");
        } else if (toml instanceof TOMLString) {
            String str = toml.toString();
            if (str.charAt(0) != '"' || str.charAt(0) != '\'') str = "\"" + str + "\"";
            builder.append(str);
        } else if (toml instanceof TOMLArray) {
            TOMLArray array = (TOMLArray) toml;
            if (array.onlyTables() && normalArrayDepth == 0) {

                for (int i = 0; i < array.size(); i++) {
                    newLine();
                    builder.append("[[");
                    printTableNameStack();
                    builder.append("]]");
                    indent();
                    newLine();
                    deIndent();
                    writeToString(array.getData(i), true);
                }
            } else {
                builder.append('[');
                if (array.size() > 0) {
                    normalArrayDepth++;
                    indent();
                    if (inlineTableDepth == 0) {
                        newLine();
                    } else {
                        builder.append(' ');
                    }
                    for (int i = 0; i < array.size(); i++) {
                        writeToString(array.getData(i), false);
                        if (i == array.size() - 1) deIndent();
                        else builder.append(',');
                        if (inlineTableDepth == 0) {
                            newLine();
                        } else {
                            builder.append(' ');
                        }
                    }
                    normalArrayDepth--;
                }
                builder.append(']');
            }
        } else if (toml instanceof TOMLTable) {
            TOMLTable table = (TOMLTable) toml;
            Map<String, TOMLValue> data = table.getAllData();
            List<String> sortedKeys = data.keySet().stream().sorted().collect(Collectors.toList());
            if (normalArrayDepth == 0) {
                boolean root = tableStack.empty();

                // append name
                if (!root) {
                    if (!skipTitle) {
                        builder.append('[');
                        printTableNameStack();
                        builder.append(']');
                    }
                    indent();
                    if (!skipTitle) newLine();
                }

                List<String> doAfter = new ArrayList<>();

                for (int i = 0; i < sortedKeys.size(); i++) {
                    String key = sortedKeys.get(i);
                    TOMLValue value = data.get(key);
                    if (value instanceof TOMLTable || (value instanceof TOMLArray) && (((TOMLArray) value).onlyTables())) doAfter.add(key);
                    else {
                        doComment(value);
                        if (value instanceof TOMLArray) tableStack.push(key);
                        addKey(key);
                        writeToString(value, false);
                        if (value instanceof TOMLArray) tableStack.pop();
                        if (i < sortedKeys.size() - 1 || doAfter.size() > 0)
                            newLine();
                    }
                }
                newLine();

                for (String key : doAfter) {
                    TOMLValue value = data.get(key);
                    doComment(value);
                    tableStack.push(TOMLString.toTOMLString(key));
                    writeToString(value, false);
                    tableStack.pop();
                }

                if (!root) deIndent();
            } else {
                builder.append("{ ");
                inlineTableDepth++;

                for (int i = 0; i < sortedKeys.size(); i++) {
                    String key = sortedKeys.get(i);
                    addKey(key);
                    writeToString(data.get(key), false);
                    if (i < sortedKeys.size() - 1) builder.append(", ");
                }
                inlineTableDepth--;
                builder.append("}");
            }
        }
    }

    private String get() {
        return builder.toString();
    }

    private void doComment(TOMLValue value) {
        if (value.getComment() != null) {
            builder.append("# ").append(value.getComment());
            newLine();
        }
    }

    /**
     * Writes the current table name stack in the form a.b.c
     */
    private void printTableNameStack() {
        builder.append(TOMLString.toTOMLString(tableStack.get(0)));
        for (int i = 1; i < tableStack.size(); i++) {
            builder.append('.');
            builder.append(TOMLString.toTOMLString(tableStack.get(i)));
        }
    }

    private void addKey(String name) {
        builder.append(TOMLString.toTOMLString(name));
        builder.append(" = ");
    }

    private void newLine() {
        builder.append('\n');
        for (int i = 0; i < indentLevel; i++) builder.append(' ');
    }
}
