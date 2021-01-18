package red.jackf.tomlconfig.parser.data;

import red.jackf.tomlconfig.exceptions.ParsingException;

/**
 * Represents a "value" node, be it an integer, boolean, array, sub-data object, etc.
 */
public interface TOMLValue {

    /**
     * Change any of the TableArrays that may in the object to standard arrays.
     */
    default void changeTableArraysToArrays() throws ParsingException {}
}
