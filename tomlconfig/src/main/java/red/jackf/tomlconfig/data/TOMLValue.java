package red.jackf.tomlconfig.data;

import red.jackf.tomlconfig.exceptions.ParsingException;

/**
 * Represents a "value" node, be it an integer, boolean, array, sub-data object, etc.
 */
public abstract class TOMLValue {
    private String comment = null;

    /**
     * Internal. Change any of the TableArrays that may in the object to standard arrays.
     * @throws ParsingException If any table arrays can not be migrated to arrays of tables.
     */
    public void changeTableArraysToArrays() throws ParsingException {}

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
