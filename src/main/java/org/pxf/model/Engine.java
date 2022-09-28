package org.pxf.model;

import org.pxf.controller.MinMax;

import java.util.ArrayList;

public class Engine {

    private static final int MIN_MAX_SEARCH_DEPTH = 3;
    private final ChessBoard  chessBoard;
    private final MinMax playerOne;
    private final MinMax playerTwo;

    public Engine() {
        chessBoard = new ChessBoard();
        playerOne = new MinMax(Team.WHITE, MIN_MAX_SEARCH_DEPTH);
        playerTwo = new MinMax(Team.BLACK, MIN_MAX_SEARCH_DEPTH);
    }

    public boolean isGameOver() {
        return chessBoard.isGameOver();
    }
    public void reset() {
        chessBoard.reset();
    }

    public boolean move(ChessBoardMove move, Team turn){
        if (chessBoard.isValidMove(move, turn)){
            chessBoard.move(move);
            return true;
        }
        return false;
    }

    public ChessBoard getBoard() {
        return chessBoard.copy();
    }

    public void minMaxStep(Team team) {
        if (isGameOver()) return;
        MinMax agent = team == playerOne.team ? playerOne : playerTwo;
        chessBoard.move(agent.getBestMove(chessBoard));
    }
    public int rollOutScore() {
        while(!isGameOver()){
            minMaxStep(Team.WHITE);
            minMaxStep(Team.BLACK);
        }
        return chessBoard.getWinner() == playerOne.team ? 1 : 0;
    }

    public ArrayList<ChessBoard> rollOut() {
        ArrayList<ChessBoard> episode = new ArrayList<>(64);
        while(!isGameOver()) {
            minMaxStep(Team.WHITE);
            minMaxStep(Team.BLACK);
            episode.add(chessBoard.copy());
        }
        return episode;
    }

    @Override
    public String toString(){
        return chessBoard.toString();
    }

}
