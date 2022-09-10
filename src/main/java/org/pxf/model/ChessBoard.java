package org.pxf.model;

import java.util.HashMap;

public class ChessBoard {
    private static final int BOARD_SIZE = 8;
    private final HashMap<ChessBoardPosition, ChessPiece> pieces = new HashMap<>();

    void initPieces() {
        initFirstRow(Team.WHITE, 0);
        initPawns(Team.WHITE, 1);

        initPawns(Team.BLACK, 6);
        initFirstRow(Team.BLACK, 7);
    }
    private void initPawns(Team team, int row) {
        for (int col = 0; col < BOARD_SIZE; col++){
            addPiece(new Pawn(team), row, col);
        }
    }
    private void initFirstRow(Team team, int row) {
        int col = 0;
        addPiece(new Rook(team), row, col++);
        addPiece(new Bishop(team), row, col++);
        addPiece(new Knight(team), row, col++);
        addPiece(new Queen(team), row, col++);
        addPiece(new King(team), row, col++);
        addPiece(new Knight(team), row, col++);
        addPiece(new Bishop(team), row, col++);
        addPiece(new Rook(team), row, col);
    }
    void addPiece(ChessPiece piece, int row, int col){
        ChessBoardPosition position = new ChessBoardPosition(row, col);
        if (isInBounds(position) && !pieces.containsKey(position) && !pieces.containsValue(piece)){
            pieces.put(position, piece);
        }
    }

    boolean isInBounds(ChessBoardPosition position) {
        return 0 <= position.row && position.row < BOARD_SIZE
                && 0 <= position.col && position.col < BOARD_SIZE;
    }

    public boolean isValidMove(ChessBoardPosition currPosition, ChessBoardPosition newPosition) {
        if (!isInBounds(newPosition))
            return false;
        if (isEmpty(newPosition))
            return true;
        return  isOpponent(currPosition, newPosition);
    }

    boolean isEmpty(ChessBoardPosition position){
        return !pieces.containsKey(position);
    }
    public boolean isOpponent(ChessBoardPosition currPosition, ChessBoardPosition newPosition) {
        return !isEmpty(newPosition) && pieces.get(currPosition).getTeam() != pieces.get(newPosition).getTeam();
    }
    public int getPiecesCount() {
        return pieces.size();
    }

    public int getPiecesCount(Team team) {
        return (int) pieces.entrySet().stream()
                .filter((entry) -> entry.getValue().getTeam() == team)
                .count();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("_".repeat(59)).append("\n");
        ChessBoardPosition currPosition;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                currPosition = new ChessBoardPosition(i,j);
                sb.append( " | " );
                if (isEmpty(currPosition)){
                    sb.append(currPosition);
                }else {
                    sb.append(pieces.get(currPosition));
                }
            }
            sb.append(" |\n");
        }
        sb.append("_".repeat(59)).append("\n");
        return sb.toString();
    }
}

