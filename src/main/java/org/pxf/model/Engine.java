package org.pxf.model;

public class Engine {
    private boolean isPlayerOneTurn = true;
    private boolean gameOver = false;

    private final ChessBoard chessBoard = new ChessBoard();
    public boolean isGameOver() {
        return gameOver;
    }

    public String getTurn() {
        return isPlayerOneTurn ? "Player 1" : "Player 2";
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

    public ChessPiece getChessPiece() {
        return new Pawn(Team.WHITE);
    }
    public String getBoardRepr(){
        return chessBoard.toString();
    }
}
