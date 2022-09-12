package org.pxf.model;

import java.util.Arrays;
import java.util.stream.Stream;

public class Direction{
    private int rowOffset;
    private int colOffset;
    Direction(Directions dir, Team team) {
        this.rowOffset = dir.rowOffset;
        this.colOffset = dir.colOffset;
        if (team == Team.BLACK)
            reverse();
    }

    void reverse(){
        rowOffset = -rowOffset;
        colOffset = -colOffset;
    }

    ChessBoardPosition step(ChessBoardPosition currCell){
        return new ChessBoardPosition(currCell.row + rowOffset, currCell.col + colOffset);
    }

    static Direction[] getQueenDirections(Team team){
        return setUp(Directions.getQueenDirections(), team);
    }
    static Direction[] getRookDirections(Team team){
        return setUp(Directions.getRookDirections(), team);
    }
    static Direction[] getBishopMoves(Team team){
        return setUp(Directions.getBishopMoves(), team);
    }
    static Direction[] getPawnDirections(Team team){
        return setUp(Directions.getPawnDirections(), team);
    }
    static Direction [] getKnightDirections(Team team){
        return setUp(Directions.getKnightDirections(), team);
    }
    static Direction[] setUp(Directions[] directions, Team team){
        return Arrays.stream(directions).map((dir) -> new Direction(dir, team)).toArray(Direction[]::new);
    }
    @Override
    public String toString(){
        return rowOffset +", " + this.colOffset;
    }
    enum Directions{
        FORWARD(1, 0),
        BACKWARD(-1, 0),
        LEFT(0, 1),
        RIGHT(0, -1),

        FORWARD_LEFT(1, 1),
        FORWARD_RIGHT(1, -1),
        BACKWARD_LEFT(-1, 1),
        BACKWARD_RIGHT(-1, -1),

        KNIGHT_A(2, 1),
        KNIGHT_B(2, -1),
        KNIGHT_C(-2, 1),
        KNIGHT_D(-2, -1),
        KNIGHT_E(1, 2),
        KNIGHT_F(1, -2),
        KNIGHT_G(-1, 2),
        KNIGHT_H(-1, -2),

        ;
        private final int rowOffset;
        private final int colOffset;
        Directions(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }
        static Directions[] getQueenDirections(){
            return Stream.of(getRookDirections() , getBishopMoves())
                    .flatMap(Stream::of).toArray(Directions[]::new);
        }
        static Directions[] getRookDirections(){
            return new Directions[]
                    {FORWARD, BACKWARD, LEFT, RIGHT};
        }
        static Directions[] getBishopMoves(){
            return new Directions[]
                    {FORWARD_LEFT, FORWARD_RIGHT, BACKWARD_LEFT, BACKWARD_RIGHT};
        }
        static Directions[] getPawnDirections(){
            return new Directions[]
                    {FORWARD, FORWARD_LEFT, FORWARD_RIGHT};
        }
        static Directions [] getKnightDirections(){
            return new Directions[]
                    {KNIGHT_A, KNIGHT_B, KNIGHT_C, KNIGHT_D, KNIGHT_E, KNIGHT_F, KNIGHT_G, KNIGHT_H};
        }

    }

}


