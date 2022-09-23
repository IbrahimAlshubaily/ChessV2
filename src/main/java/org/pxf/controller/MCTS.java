package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class MCTS {
    public final Team team;
    private final int nSamples;
    //private final LRUCache cache;

    public MCTS(Team team, int nSamples){
        this.team = team;
        this.nSamples = nSamples;
        //this.cache = new LRUCache(1024 * 1024);

    }

    public ChessBoardMove getBestMove(ChessBoard board) {
        int depth = 5;
        return board.getMoves(team)
                .parallelStream()
                .max(Comparator.comparing(move -> randomWalk(board.parallelMove(move), depth)))
                .orElseThrow(NoSuchElementException::new);
    }

    private float randomWalk(ChessBoard board, int depth) {
        float score = 0;
        for (int i = 0; i < nSamples; i++){
            score += randomWalk(board, getOpponent(team), depth - 1);
        }
        return score;
    }

    private float randomWalk(ChessBoard board, Team team, int depth) {
        if (depth == 0 || board.isGameOver()) {
            return eval(board);
        }
        ChessBoard nextBoard = board.parallelMove(getRandomMove(board, team));
        return randomWalk(nextBoard, getOpponent(team), depth - 1);
        //float winRatio =  randomWalk(nextBoard, getOpponent(team), depth - 1);
        //cache.put(nextBoard, winRatio);
        //return cache.get(nextBoard);
    }

    private ChessBoardMove getRandomMove(ChessBoard board, Team team){
        ArrayList<ChessBoardMove> moves = board.getMoves(team);
        return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));
    }

    private int eval(ChessBoard board) {
        return board.getWinner() == this.team ? 1 : 0;
    }

    private static Team getOpponent(Team team) {
        return team == Team.WHITE? Team.BLACK: Team.WHITE;
    }
}
