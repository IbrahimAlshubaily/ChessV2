package org.pxf.model;

import java.util.ArrayList;

public class ChessPiece {
    private final Team team;
    private final Direction[] directions;
    private final int nSteps;
    private final String repr;
    private final int heuristicValue;

    public ChessPiece(Team team, String repr, Direction[] directions, int nSteps, int heuristicValue) {
        this.team = team;
        this.repr = repr;
        this.nSteps = nSteps;
        this.directions = directions;
        this.heuristicValue = heuristicValue;
    }
    //Break me down. pls.
    public ArrayList<ChessBoardMove> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        ArrayList<ChessBoardMove> result = new ArrayList<>();
        for (Direction dir : this.directions){
            result.addAll(getMovesInDirection(board, currPosition, dir));
        }
        return result;
    }
    private ArrayList<ChessBoardMove> getMovesInDirection(ChessBoard board, ChessBoardPosition currPosition, Direction dir){
        ArrayList<ChessBoardMove> result = new ArrayList<>();
        ChessBoardMove currMove;
        ChessBoardPosition newPosition = new ChessBoardPosition(currPosition);
        for (int i = 0; i < this.nSteps; i++){
            newPosition = dir.step(newPosition, team);
            currMove = new ChessBoardMove(currPosition, newPosition);
            if (board.isValidMove(currMove)) {
                result.add(currMove);
                if (!board.isEmpty(newPosition)) break;
            } else break;
        }
        return result;
    }

    public Team getTeam() { return team; }
    public boolean isKing() { return repr.equalsIgnoreCase("K"); }
    @Override
    public String toString() { return " " + team.toString().charAt(0) + repr.toUpperCase() + " "; }

    public int getHeuristicValue() {
        return heuristicValue;
    }
}

class Queen extends ChessPiece{
    Queen(Team team){ super(team, "Q", Direction.getQueenDirections(), 8, 10); }
}
class King extends ChessPiece{
    King(Team team){ super(team, "K", Direction.getQueenDirections(), 1, Integer.MAX_VALUE / 2); }
}
class Knight extends ChessPiece{
    Knight(Team team){ super(team, "N", Direction.getKnightDirections(), 1, 5); }
}
class Bishop extends ChessPiece{

    Bishop(Team team){ super(team, "B", Direction.getBishopMoves(), 8, 5); }

}
class Rook extends ChessPiece{
    Rook(Team team){ super(team, "R", Direction.getRookDirections(), 8, 5); }
}
class Pawn extends ChessPiece{
    public Pawn(Team team){ super(team, "P", new Direction []{Direction.FORWARD}, 2, 5); }
    public ArrayList<ChessBoardMove> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        ArrayList<ChessBoardMove> result = super.getMoves(board, currPosition);
        result.addAll(getDiagonalMoves(board, currPosition));
        return result;
    }
    private ArrayList<ChessBoardMove> getDiagonalMoves(ChessBoard board, ChessBoardPosition currPosition){
        ArrayList<ChessBoardMove> result = new ArrayList<>(2);
        ChessBoardPosition newPosition;
        ChessBoardMove currMove;
        for (Direction dir : new Direction[]{Direction.FORWARD_LEFT, Direction.FORWARD_RIGHT}) {
            newPosition = dir.step(currPosition, getTeam());
            currMove = new ChessBoardMove(currPosition, newPosition);
            if (isValidDiagonalMove(board, currMove)){
                result.add(currMove);
            }
        }
        return result;
    }
    private boolean isValidDiagonalMove(ChessBoard board, ChessBoardMove currMove){
        return  board.isInBounds(currMove.destination) && board.isOpponent(currMove);
    }

}