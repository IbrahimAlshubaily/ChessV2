package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class MinMax {
    private final int searchDepth;
    final Team team;

    public MinMax(Team team, int searchDepth){
        this.searchDepth = searchDepth;
        this.team = team;
    }
    public ChessBoardMove getBestMove(ChessBoard board) {
        return board.getMoves(team)
                .parallelStream()
                .max(Comparator.comparing(move -> min(board.parallelMove(move), searchDepth - 1)))
                .orElseThrow(NoSuchElementException::new);
    }
    private int min(ChessBoard board, int depth){
        if (board.isGameOver() || depth == 0)
            return board.eval(team);
        return board.getMoves(getOpponent(team))
                .parallelStream()
                .mapToInt((move) -> max(board.parallelMove(move), depth - 1))
                .min().orElseThrow(NoSuchElementException::new);
    }
    private int max(ChessBoard board, int depth ){
        if (board.isGameOver() || depth == 0)
            return board.eval(team);
        return board.getMoves(team)
                .parallelStream()
                .mapToInt((move) -> min(board.parallelMove(move), depth - 1))
                .max().orElseThrow(NoSuchElementException::new);
    }

    private Team getOpponent(Team team) {
        return team == Team.WHITE? Team.BLACK: Team.WHITE;
    }
}
