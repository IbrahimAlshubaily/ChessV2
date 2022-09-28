package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class MinMax {
    public final Team team;
    private final int searchDepth;

    public MinMax(Team team, int searchDepth){
        this.searchDepth = searchDepth;
        this.team = team;
    }

    public ChessBoardMove getBestMove(ChessBoard board) {
        return board.getMoves(team)
                .parallel()
                .max(Comparator.comparing(move -> min(board.moveWithoutMutating(move), searchDepth - 1)))
                .orElseThrow(NoSuchElementException::new);
    }
    private int min(ChessBoard board, int depth){
        if (board.isGameOver() || depth == 0)
            return board.eval(team);
        return board.getMoves(team.getOpponent())
                .mapToInt((move) -> max(board.moveWithoutMutating(move), depth - 1))
                .min().orElseThrow(NoSuchElementException::new);
    }
    private int max(ChessBoard board, int depth ){
        if (board.isGameOver() || depth == 0)
            return board.eval(team);
        return board.getMoves(team)
                .mapToInt((move) -> min(board.moveWithoutMutating(move), depth - 1))
                .max().orElseThrow(NoSuchElementException::new);
    }
}
