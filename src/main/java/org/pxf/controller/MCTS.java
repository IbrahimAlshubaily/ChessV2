package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class MCTS {
    public final Team team;
    private final int nSamples;

    public MCTS(Team team, int nSamples){
        this.team = team;
        this.nSamples = nSamples;
    }

    public ChessBoardMove getBestMove(ChessBoard board) {
        int depth = 5;
        return board.getMoves(team)
                .max(Comparator.comparing(move -> randomWalk(board.moveWithoutMutating(move), depth)))
                .orElseThrow(NoSuchElementException::new);
    }

    private int randomWalk(ChessBoard board, int depth) {
        int score = 0;
        for (int i = 0; i < nSamples; i++){
            score += randomWalk(board, team.getOpponent(), depth - 1);
        }
        return score;
    }

    private int randomWalk(ChessBoard board, Team team, int depth) {
        if (depth == 0 || board.isGameOver()) {
            return eval(board);
        }
        ChessBoard nextBoard = board.moveWithoutMutating(getRandomMove(board, team));
        return randomWalk(nextBoard, team.getOpponent(), depth - 1);
    }

    private ChessBoardMove getRandomMove(ChessBoard board, Team team){
        return board.getMoves(team).findAny().get();
    }

    private int eval(ChessBoard board) {
        return board.getWinner() == this.team ? 1 : 0;
    }


}
