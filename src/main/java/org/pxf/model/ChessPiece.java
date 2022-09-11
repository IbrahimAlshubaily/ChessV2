package org.pxf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ChessBoardMove> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        return Arrays.stream(this.directions).parallel()
                .map((dir) -> getMovesInDirection(board, currPosition, dir))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    private ArrayList<ChessBoardMove> getMovesInDirection(ChessBoard board, ChessBoardPosition currPosition, Direction dir){
        ArrayList<ChessBoardMove> result = new ArrayList<>();
        ChessBoardMove currMove = new ChessBoardMove(currPosition, currPosition);
        for (int i = 0; i < this.nSteps; i++){
            currMove = currMove.step(dir, team);
            if (board.isValidMove(currMove)) {
                result.add(currMove);
                if (!board.isEmpty(currMove.destination)) break;
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
    ArrayList<Direction> diagonalDirections;
    public Pawn(Team team){
        super(team, "P", new Direction []{Direction.FORWARD}, 2, 5);
        this.diagonalDirections = new ArrayList<>(List.of(Direction.FORWARD_LEFT, Direction.FORWARD_RIGHT));
    }
    public List<ChessBoardMove> getMoves(ChessBoard board, ChessBoardPosition currPosition){
        ArrayList<ChessBoardMove> result = new ArrayList<>(4);
        ChessBoardMove currMove = new ChessBoardMove(currPosition, currPosition).step(Direction.FORWARD, getTeam());
        if (board.isEmpty(currMove.destination)) {
            result.add(currMove);
            currMove = currMove.step(Direction.FORWARD, getTeam());
            if (board.isEmpty(currMove.destination))
                result.add(currMove);
        }

        diagonalDirections.forEach((direction) -> {
            ChessBoardMove diagonalMove = new ChessBoardMove(currPosition, direction.step(currPosition, getTeam()));
            if (isValidDiagonalMove(board, diagonalMove))
                result.add(diagonalMove);
        });
        return result;
    }
    private boolean isValidDiagonalMove(ChessBoard board, ChessBoardMove currMove){
        return  board.isInBounds(currMove.destination) && board.isOpponent(currMove);
    }

}