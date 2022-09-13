package org.pxf.model;

import org.pxf.controller.MinMax;

import javax.swing.*;
import java.util.List;
import java.util.Map;

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

    public void minMaxRollout() {
        int searchDepth = 3;
        while(!isGameOver()){
            System.out.println(getPiecesCount());
            chessBoard.move(MinMax.getBestMove(chessBoard, Team.WHITE, searchDepth));
            if (!isGameOver())
                chessBoard.move(MinMax.getBestMove(chessBoard, Team.BLACK, searchDepth));
            System.out.println(getBoardRepr());
        }
    }
    public void minMaxRollout(JPanel panel) {
        int searchDepth = 3;
        //int sleepDurationSeconds = 3;
        while(!isGameOver()){
            //TimeUnit.SECONDS.sleep(sleepDurationSeconds);
            System.out.println(getPiecesCount("player 1")  +" Vs. " + getPiecesCount("player 2"));
            chessBoard.move(MinMax.getBestMove(chessBoard, Team.BLACK, searchDepth));
            panel.repaint();
            if (!isGameOver())
                chessBoard.move(MinMax.getBestMove(chessBoard, Team.WHITE, searchDepth));
            panel.repaint();
        }
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

    public Map<ChessBoardPosition, ChessPiece> getChessPieces() {
        return chessBoard.getPieces();
    }
}
