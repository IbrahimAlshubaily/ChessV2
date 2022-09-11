package org.pxf.model;

import org.pxf.controller.MinMax;

import java.util.ArrayList;

public class Engine {
    private final ChessBoard chessBoard = new ChessBoard();
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
        int searchDepth;
        int step = 0;
        while(!isGameOver()){
            searchDepth = step++ < 10 ? 3 : 5;
            System.out.println(getPiecesCount());
            chessBoard.move(MinMax.getBestMove(chessBoard, Team.WHITE, searchDepth));
            if (!isGameOver())
                chessBoard.move(MinMax.getBestMove(chessBoard, Team.BLACK, searchDepth));
            System.out.println(getBoardRepr());
        }
    }

    public String getBoardRepr(){
        return chessBoard.toString();
    }

    public ArrayList<ChessBoardMove> getMoves(ChessPiece piece) {
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
}
