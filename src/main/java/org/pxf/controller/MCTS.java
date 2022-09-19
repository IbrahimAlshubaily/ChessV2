package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class MCTS {
    private final int nSamples;
    public final Team team;
    private final LRUCache cache;

    public MCTS(Team team, int nSamples){
        this.team = team;
        this.nSamples = nSamples;
        this.cache = new LRUCache(0);

    }
    public ChessBoardMove getBestMove(ChessBoard board) {
        return board.getMoves(team)
                .parallelStream()
                .max(Comparator.comparing(move -> randomWalk(board.parallelMove(move))))
                .orElseThrow(NoSuchElementException::new);
    }
    private int randomWalk(ChessBoard board) {
        int score = 0;
        for (int i = 0; i < nSamples; i++){
            score += randomWalk(board, getOpponent(team));
        }
        return score;
    }
    private int randomWalk(ChessBoard board, Team team) {
        if (board.isGameOver())
            return eval(board);
        return randomWalk(board.parallelMove(getRandomMove(board, team)), getOpponent(team));
    }
    private ChessBoardMove getRandomMove(ChessBoard board, Team team){
        ArrayList<ChessBoardMove> moves = board.getMoves(team);
        return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));
    }
    private int eval(ChessBoard board) {
        return board.getWinner() == this.team ? 1 : -1;
    }
    private static Team getOpponent(Team team) {
        return team == Team.WHITE? Team.BLACK: Team.WHITE;
    }
}
