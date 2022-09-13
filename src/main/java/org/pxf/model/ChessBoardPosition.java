package org.pxf.model;

public class ChessBoardPosition {
    public final int row;
    public final int col;

    public ChessBoardPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (!(other instanceof ChessBoardPosition))
            return false;

        ChessBoardPosition otherPosition = (ChessBoardPosition) other;
        return row == otherPosition.row && col == otherPosition.col;
    }

    @Override
    public final int hashCode() {
        return 42 * Integer.hashCode(row) + 7 * Integer.hashCode(col);

    }

    @Override
    public String toString(){
        return row + "  " + col;
    }
}
