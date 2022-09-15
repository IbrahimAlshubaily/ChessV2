package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.stream.IntStream.range;

public class MCTS {
    private int nSamples;
    public final Team team;
    public MCTS(Team team, int nSamples){
        this.team = team;
        this.nSamples = nSamples;
    }
    public ChessBoardMove getBestMove(ChessBoard board) {
        nSamples = Math.min(nSamples * 2, 512);
        System.out.println(nSamples);
        return board.getMoves(team)
                .parallelStream()
                .max(Comparator.comparing(move -> randomWalk(board.parallelMove(move))))
                .orElseThrow(NoSuchElementException::new);
    }
    private int randomWalk(ChessBoard board) {
        return range(0, nSamples).parallel().map((i) -> randomWalk(board, getOpponent(team))).sum();
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
        return board.getWinner() == this.team ? 1 : 0;
    }
    private static Team getOpponent(Team team) {
        return team == Team.WHITE? Team.BLACK: Team.WHITE;
    }
}
