package org.pxf.controller;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardMove;
import org.pxf.model.Team;

import java.util.NoSuchElementException;


public class MinMax {
    public static ChessBoardMove getBestMove(ChessBoard board, Team playerOneTeam, int depth) {
        int maxVal = Integer.MIN_VALUE;
        ChessBoardMove bestMove = null;
        for (ChessBoardMove move : board.getMoves(playerOneTeam)){
            int val = min(board.parallelMove(move), playerOneTeam, depth - 1);
            if (val > maxVal) {
                maxVal = val;
                bestMove = move;
            }
        }
        return bestMove;
    }
    private static int min(ChessBoard board, Team playerOneTeam, int depth ){
        if (board.isGameOver() || depth == 0)
            return eval(board, playerOneTeam);
        return board.getMoves(getOpponent(playerOneTeam))
                .parallelStream()
                .mapToInt((move) -> max(board.parallelMove(move), playerOneTeam, depth - 1))
                .min().orElseThrow(NoSuchElementException::new);
    }
    private static int max(ChessBoard board, Team playerOneTeam, int depth ){
        if (board.isGameOver() || depth == 0)
            return eval(board, playerOneTeam);
        return board.getMoves(playerOneTeam)
                .parallelStream()
                .mapToInt((move) -> min(board.parallelMove(move), playerOneTeam, depth - 1))
                .max().orElseThrow(NoSuchElementException::new);
    }

    private static int eval(ChessBoard board, Team team) {
        return board.getPiecesHeuristicValue(team) - board.getPiecesHeuristicValue(getOpponent(team));
    }
    private static Team getOpponent(Team team) {
        return team == Team.WHITE? Team.BLACK: Team.WHITE;
    }
}
