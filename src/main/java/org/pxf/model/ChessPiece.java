package org.pxf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ChessPiece {
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

    public List<ChessBoardMove> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        return Arrays.stream(this.directions).parallel()
                .map((dir) -> getMovesInDirection(board, currPosition, dir))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    private ArrayList<ChessBoardMove> getMovesInDirection(ChessBoard board, ChessBoardPosition currPosition, Direction dir){
        int nSteps = this.nSteps;
        ArrayList<ChessBoardMove> result = new ArrayList<>();
        ChessBoardMove currMove = new ChessBoardMove(currPosition, currPosition).step(dir);
        while (board.isValidMove(currMove) && nSteps-- > 0 ){
            result.add(currMove);
            if (board.isOpponent(currMove)) break;
            currMove = currMove.step(dir);
        }
        return result;
    }

    public Team getTeam() { return team; }
    public boolean isKing() { return repr.equalsIgnoreCase("K"); }
    public boolean isPawn() { return repr.equalsIgnoreCase("P"); }

    @Override
    public String toString() { return " " + team.toString().charAt(0) + repr.toUpperCase() + " "; }

    public int getHeuristicValue() {
        return heuristicValue;
    }
}

class Queen extends ChessPiece{
    Queen(Team team){ super(team, "Q", Direction.getQueenDirections(team), 8, 10); }
}
class King extends ChessPiece{
    King(Team team){ super(team, "K", Direction.getQueenDirections(team), 1, Integer.MAX_VALUE / 2); }
}
class Knight extends ChessPiece{
    Knight(Team team){ super(team, "N", Direction.getKnightDirections(team), 1, 5); }
}
class Bishop extends ChessPiece{

    Bishop(Team team){ super(team, "B", Direction.getBishopMoves(team), 8, 5); }

}
class Rook extends ChessPiece{
    Rook(Team team){ super(team, "R", Direction.getRookDirections(team), 8, 5); }
}
class Pawn extends ChessPiece{
    private final Direction[] directions;
    public Pawn(Team team){
        super(team, "P", null, 2, 1);
        this.directions = Direction.getPawnDirections(team);
    }
    public List<ChessBoardMove> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        ChessBoardMove baseMove = new ChessBoardMove(currPosition, currPosition);
        ArrayList<ChessBoardMove> result = getForwardMoves(board, baseMove, directions[0]);
        addDiagonalMove(board, baseMove.step(directions[1]), result);
        addDiagonalMove(board, baseMove.step(directions[2]), result);
        return result;
    }

    private ArrayList<ChessBoardMove> getForwardMoves(ChessBoard board, ChessBoardMove baseMove, Direction forward) {
        ArrayList<ChessBoardMove> result = new ArrayList<>(4);
        ChessBoardMove currMove = baseMove.step(forward);
        if (isValidForwardMove(board, currMove)){
            result.add(currMove);
            currMove = currMove.step(forward);
            if (isValidForwardMove(board, currMove))
                result.add(currMove);
        }
        return result;
    }

    private void addDiagonalMove(ChessBoard board, ChessBoardMove currMove, ArrayList<ChessBoardMove> result){
        if (isValidDiagonalMove(board, currMove)){
            result.add(currMove);
        }
    }

    private Boolean isValidForwardMove(ChessBoard board, ChessBoardMove currMove) {
        return board.isInBounds(currMove.destination) && board.isEmpty(currMove.destination);
    }
    private boolean isValidDiagonalMove(ChessBoard board, ChessBoardMove currMove){
        return  board.isInBounds(currMove.destination) && board.isOpponent(currMove);
    }

}