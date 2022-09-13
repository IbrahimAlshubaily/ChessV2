package org.pxf.model;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChessBoard {
    private static final int BOARD_SIZE = 8;
    private boolean gameOver = false;
    private final HashMap<ChessBoardPosition, ChessPiece> pieces;

    public ChessBoard(){
        this.pieces = new HashMap<>();
    }
    public ChessBoard(HashMap<ChessBoardPosition, ChessPiece> pieces) {
        this.pieces = pieces;
    }

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
        if (isInBounds(position) && !pieces.containsKey(position)){
            pieces.put(position, piece);
        }
    }

    public boolean isGameOver() { return gameOver; }

    boolean isValidMove(ChessBoardMove move) {
        return !isEmpty(move.source) &&
                isInBounds(move.destination) &&
                (isEmpty(move.destination) || isOpponent(move));
    }
    boolean isInBounds(ChessBoardPosition position) {
        return 0 <= position.row && position.row < BOARD_SIZE
                && 0 <= position.col && position.col < BOARD_SIZE;
    }
    boolean isEmpty(ChessBoardPosition position){
        return !pieces.containsKey(position);
    }

    boolean isOpponent(ChessBoardMove move) {
        return !isEmpty(move.destination) &&
                pieces.get(move.source).getTeam() != pieces.get(move.destination).getTeam();
    }
    ChessPiece getPiece(ChessBoardPosition position){
        return pieces.get(position);
    }
    Map<ChessBoardPosition, ChessPiece> getPieces() {
        return pieces;
    }

    public int getPiecesCount() {
        return pieces.size();
    }

    public int getPiecesCount(Team team) { return (int) getTeamStream(team).count();}

    public int getPiecesHeuristicValue(Team team) {
        return getTeamStream(team).flatMapToInt(
                (entry) -> IntStream.of(entry.getValue().getHeuristicValue())).sum();
    }
    private Stream<Entry<ChessBoardPosition, ChessPiece>> getTeamStream(Team team){
        return pieces.entrySet().stream().filter((entry) -> entry.getValue().getTeam() == team);
    }

    public List<ChessBoardMove> getMoves(ChessPiece piece) {
        return piece.getMoves(this, getPiecePosition(piece));
    }
    public ArrayList<ChessBoardMove> getMoves(Team team) {
        ArrayList<ChessBoardMove> moves = getTeamStream(team)
                .map((entry) -> entry.getValue().getMoves(this, entry.getKey()))
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(moves);
        return moves;
    }
    private ChessBoardPosition getPiecePosition(ChessPiece piece){
        if (!pieces.containsValue(piece)) return null;
        return pieces.entrySet().parallelStream()
                .filter(entry -> entry.getValue().equals(piece))
                .map(Entry::getKey).iterator().next();
    }
    public boolean move(ChessPiece piece, ChessBoardPosition newPosition) {
        ChessBoardPosition currPosition = getPiecePosition(piece);
        ChessBoardMove move = new ChessBoardMove(currPosition, newPosition);
        if (piece.getMoves(this, currPosition).contains(move)){
            move(move);
            return true;
        }
        return false;
    }

    public void move(ChessBoardMove move) {
        if (pieces.containsKey(move.destination) && pieces.get(move.destination).isKing())
            gameOver = true;
        pieces.put(move.destination, pieces.remove(move.source));
    }

    public ChessBoard parallelMove(ChessBoardMove move) {
        ChessBoard newBoard = new ChessBoard((HashMap<ChessBoardPosition, ChessPiece>) pieces.clone());
        newBoard.move(move);
        return newBoard;
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

