package org.pxf.model;

import org.pxf.controller.MCTS;
import org.pxf.controller.MinMax;

import java.util.ArrayList;
import java.util.List;

public class Engine {
    private final ChessBoard chessBoard = new ChessBoard();
    private Team currPlayerTurn = Team.BLACK;
    public boolean isGameOver() {
        return chessBoard.isGameOver();
    }

    public int getPiecesCount() {
        return chessBoard.getPiecesCount();
    }

    public int getPiecesCount(String player) {
        return chessBoard.getPiecesCount(
                player.equalsIgnoreCase("player 1") ? Team.BLACK : Team.WHITE);
    }

    public void initPieces(){
        chessBoard.initPieces();
    }

    public void addPiece(ChessPiece chessPiece, int row, int col) {
        chessBoard.addPiece(chessPiece, row, col);
    }

    public void move(ChessPiece piece, ChessBoardPosition newPosition){
        if (chessBoard.move(piece, newPosition))
            currPlayerTurn = getOpponent(currPlayerTurn);
    }
    public String getBoardRepr(){
        return chessBoard.toString();
    }

    public List<ChessBoardMove> getMoves(ChessPiece piece) {
        return chessBoard.getMoves(piece);
    }

    public ChessPiece getChessPiece() {
        return new Pawn(Team.WHITE);
    }

    public ChessPiece getChessPiece(String pieceName, Team team) {
        if (pieceName.equalsIgnoreCase("pawn")){
            return new Pawn(team);
        }
        return null;
    }
    public ChessPiece getChessPiece(ChessBoardPosition position) {
        if (chessBoard.getPiece(position).getTeam() == currPlayerTurn){
            return chessBoard.getPiece(position);
        }
        return null;
    }

    private Team getOpponent(Team team){
        return team == Team.BLACK? Team.WHITE : Team.BLACK;
    }


    public ArrayList<ChessBoard> rollOut(int minMaxDepth, int mcts_nSamples) {
        ArrayList<ChessBoard> episode = new ArrayList<>(64);
        MCTS mctsAgent = new MCTS(Team.WHITE, mcts_nSamples);
        MinMax minMaxAgent = new MinMax(Team.BLACK, minMaxDepth);
        while(!isGameOver()){
            chessBoard.move(minMaxAgent.getBestMove(chessBoard));
            episode.add(chessBoard.copy());
            if (!isGameOver())
                chessBoard.move(mctsAgent.getBestMove(chessBoard));
            episode.add(chessBoard.copy());
        }
        return episode;
    }

    public int rollOutScore(int minMaxDepth, int mcts_nSamples) {
        MCTS player1 = new MCTS(Team.WHITE, mcts_nSamples);
        MinMax player2 = new MinMax(Team.BLACK, minMaxDepth);
        while(!isGameOver()){
            chessBoard.move(player1.getBestMove(chessBoard));
            if (!isGameOver())
                chessBoard.move(player2.getBestMove(chessBoard));
        }
        return chessBoard.getWinner() == player1.team ? 1 : 0;
    }
}
