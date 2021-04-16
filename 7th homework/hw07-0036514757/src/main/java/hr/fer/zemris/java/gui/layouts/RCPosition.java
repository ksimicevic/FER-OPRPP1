package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

public class RCPosition {
    private int row;
    private int column;

    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static RCPosition parse(String text) {
        String input = text.trim();
        int indexOfSeparator = input.indexOf(",");

        if (indexOfSeparator < 0) {
            throw new IllegalArgumentException("Given input does not contain a comma separator, therefore it is not a valid input.");
        }

        int row, column;

        try {
            row = Integer.parseInt(input.substring(0, indexOfSeparator));
            column = Integer.parseInt(input.substring(indexOfSeparator + 1));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Input is invalid. Couldn't parse numbers from input.");
        }

        return new RCPosition(row, column);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RCPosition that = (RCPosition) o;
        return row == that.row &&
                column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
