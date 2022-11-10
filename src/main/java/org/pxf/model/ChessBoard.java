package org.pxf.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessBoard {
    private static final int BOARD_SIZE = 8;
    private boolean gameOver;
    private Team winner;
    private HashMap<ChessBoardPosition, ChessPiece> pieces;

    //recycle the board
    public ChessBoard(){
        reset();
    }
    public void reset() {
        initPieces();
        this.gameOver = false;
        this.winner = null;
    }
    @SuppressWarnings("unchecked")
    public ChessBoard(ChessBoard board) {
        this.pieces = (HashMap<ChessBoardPosition, ChessPiece>) board.pieces.clone();
        this.gameOver = board.gameOver;
        this.winner = board.winner;
    }
    void initPieces() {
        pieces = new HashMap<>();
        initFirstRow(Team.WHITE, 0);
        initPawns(Team.WHITE, 1);

        initPawns(Team.BLACK, 6);
        initFirstRow(Team.BLACK, 7);
    }
    private void initPawns(Team team, int row) {
        for (int col = 0; col < BOARD_SIZE; col++){
            add(new Pawn(team), row, col);
        }
    }
    private void initFirstRow(Team team, int row) {
        int col = 0;
        add(new Rook(team), row, col++);
        add(new Bishop(team), row, col++);
        add(new Knight(team), row, col++);
        add(new Queen(team), row, col++);
        add(new King(team), row, col++);
        add(new Knight(team), row, col++);
        add(new Bishop(team), row, col++);
        add(new Rook(team), row, col);
    }

    void add(ChessPiece piece, int row, int col){
        ChessBoardPosition position = new ChessBoardPosition(row, col);
        if (isInBounds(position) && !pieces.containsKey(position)){
            pieces.put(position, piece);
        }
    }

    void move(ChessBoardMove move) {

        if (pieces.containsKey(move.destination) && pieces.get(move.destination).isKing()) {
            gameOver = true;
            winner = pieces.get(move.source).getTeam();
        }

        pieces.put(move.destination, pieces.remove(move.source));
        if (pieces.get(move.destination).isPawn()){
            ChessPiece pawn = pieces.get(move.destination);
            if ((move.destination.row == 0 && pawn.getTeam() == Team.BLACK) ||
                    (move.destination.row == 7 && pawn.getTeam() == Team.WHITE)){
                pieces.put(move.destination, new Queen(pawn.getTeam()));
            }
        }
    }

    public boolean isGameOver() { return gameOver; }

    public Team getWinner() { return winner; }

    boolean isValidMove(ChessBoardMove move) {
        return !isEmpty(move.source) &&
                isInBounds(move.destination) &&
                (isEmpty(move.destination) || isOpponent(move));
    }

    boolean isValidMove(ChessBoardMove move, Team turn) {
        ChessPiece piece = pieces.get(move.source);
        if (piece.getTeam() == turn)
            return  piece.getMoves(this, move.source).anyMatch(move::equals);
        return false;
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

    public Map<ChessBoardPosition, ChessPiece> getPieces() {
        return pieces;
    }
    private Stream<Entry<ChessBoardPosition, ChessPiece>> getTeamStream(Team team){
        return pieces.entrySet().stream()
                .filter((entry) -> entry.getValue().getTeam() == team)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Collections.shuffle(list);
                            return list;
                        }
                )).stream();
    }

    public Stream<ChessBoardMove> getMoves(Team team) {
        return getTeamStream(team)
                .flatMap((entry) -> entry.getValue().getMoves(this, entry.getKey()));
    }

    public ChessBoard moveWithoutMutating(ChessBoardMove move) {
        ChessBoard newBoard = copy();
        newBoard.move(move);
        return newBoard;
    }

    public ChessBoard copy() {
        return new ChessBoard(this);
    }

    public int eval(Team team) {
        return this.getPiecesHeuristicValue(team) - this.getPiecesHeuristicValue(team.getOpponent());
    }

    private int getPiecesHeuristicValue(Team team) {
        return getTeamStream(team).mapToInt(
                (entry) -> entry.getValue().getHeuristicValue()).sum();
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

