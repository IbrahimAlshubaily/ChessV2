package org.pxf.model;

public class ChessBoardMove {
    final ChessBoardPosition source;
    final ChessBoardPosition destination;

    public ChessBoardMove(ChessBoardPosition source, ChessBoardPosition destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (!(other instanceof ChessBoardMove))
            return false;

        ChessBoardMove otherMove = (ChessBoardMove) other;
        return source.equals(otherMove.source) && destination.equals(otherMove.destination);
    }

    @Override
    public final int hashCode() {
        return 42 * source.hashCode() + 7 * destination.hashCode();

    }
    @Override
    public String toString(){
        return source + "  ->  " + destination;
    }


}
