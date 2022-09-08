package org.pxf.model;

import java.util.ArrayList;

public class ChessPiece {
    private final Team team;
    private final Direction[] directions;
    private final int nSteps;
    private final String repr;
    public ChessPiece(Team team, String repr, Direction[] directions, int nSteps) {
        this.team = team;
        this.repr = repr;
        this.nSteps = nSteps;
        this.directions = directions;
    }
    public ArrayList<ChessBoardPosition> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        ArrayList<ChessBoardPosition> result = new ArrayList<>();
        ChessBoardPosition newPosition;
        for (Direction dir : this.directions){
            newPosition = new ChessBoardPosition(currPosition.row, currPosition.col);
            for (int i = 1; i <= this.nSteps; i++){
                newPosition = dir.step(newPosition, team);
                if (board.isInBounds(newPosition)){
                    if (board.isValidMove(currPosition, newPosition)) {
                        result.add(newPosition);
                        if (board.isEmpty(newPosition)) break;
                    }else break;
                }else break;
            }
        }
        return result;
    }
    public Team getTeam() { return team; }
    public String getRepr() { return team.toString().charAt(0) + repr.toUpperCase(); }
    public boolean isKing() { return repr.equalsIgnoreCase("K"); }
}

class Queen extends ChessPiece{
    Queen(Team team){ super(team, "Q", Direction.getDirections(), 8); }
}
class King extends ChessPiece{
    King(Team team){ super(team, "K", Direction.getDirections(), 1); }
}
class Knight extends ChessPiece{
    Knight(Team team){ super(team, "N", Direction.getKnightDirections(), 1); }
}
class Bishop extends ChessPiece{

    Bishop(Team team){ super(team, "B", new Direction []{Direction.FORWARD_LEFT, Direction.FORWARD_RIGHT,
            Direction.BACKWARD_LEFT, Direction.BACKWARD_RIGHT}, 8); }

}
class Rook extends ChessPiece{
    Rook(Team team){ super(team, "R", new Direction []{Direction.FORWARD, Direction.BACKWARD,
            Direction.LEFT, Direction.RIGHT}, 8); }
}
class Pawn extends ChessPiece{
    Pawn(Team team){ super(team, "P", new Direction []{Direction.FORWARD}, 2); }
    public ArrayList<ChessBoardPosition> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        ArrayList<ChessBoardPosition> result = new ArrayList<>();
        ChessBoardPosition newPosition = new ChessBoardPosition(currPosition.row, currPosition.col);
        Direction forward = Direction.FORWARD;
        for (int i = 0; i < 2; i++){
            newPosition = forward.step(newPosition, getTeam());
            if (isValidForwardMove(board, newPosition)) {
                result.add(newPosition);
            }else break;
        }
        
        for (Direction dir : new Direction[]{Direction.FORWARD_LEFT, Direction.FORWARD_RIGHT}) {
            newPosition = dir.step(currPosition, getTeam());
            if (isValidDiagonalMove(board, currPosition, newPosition)){
                result.add(newPosition);
            }
        }
        return result;
    }
    private boolean isValidForwardMove(ChessBoard board, ChessBoardPosition newPosition){
        return board.isInBounds(newPosition) && board.isEmpty(newPosition);
    }
    private boolean isValidDiagonalMove(ChessBoard board, ChessBoardPosition currPosition, ChessBoardPosition newPosition){
        return  board.isInBounds(newPosition) && board.isOpponent(currPosition, newPosition);
    }

}