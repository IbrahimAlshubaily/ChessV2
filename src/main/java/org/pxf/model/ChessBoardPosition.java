package org.pxf.model;

class ChessBoardPosition {
    final int row;
    final int col;

    ChessBoardPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    ChessBoardPosition(ChessBoardPosition position) {
        this.row = position.row;
        this.col = position.col;
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
}
