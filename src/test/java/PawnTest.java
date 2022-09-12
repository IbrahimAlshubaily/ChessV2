import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pxf.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTest {

    private Engine engine;
    @BeforeEach
    void initEngine(){
        engine = new Engine();
    }

    @Test
    void testPawnForwardMoves(){
        //engine.addPiece(whitePawn, 5, 4);
        //actualMoves = engine.getMoves(blackPawn);
        //assertEquals(0, actualMoves.size());
    }
    @Test
    void testWhitePawnForwardMoves(){
        ChessPiece pawn = engine.getChessPiece("Pawn", Team.WHITE);
        engine.addPiece(pawn, 1, 4);
        List<ChessBoardMove> actualMoves = engine.getMoves(pawn);

        ArrayList<ChessBoardMove> expectedMoves = new ArrayList<>();
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(1, 4), new ChessBoardPosition(2, 4)));
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(1, 4), new ChessBoardPosition(3, 4)));
        assertEquals(expectedMoves, actualMoves);

    }
    @Test
    void testBlackPawnForwardMoves(){
        ChessPiece pawn = engine.getChessPiece("Pawn", Team.BLACK);
        engine.addPiece(pawn, 6, 4);

        ArrayList<ChessBoardMove> expectedMoves = new ArrayList<>();
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(5, 4)));
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(4, 4)));
        assertEquals(expectedMoves, engine.getMoves(pawn));
    }

    @Test
    void testPawnBlockedOneForwardMove(){
        ChessPiece pawn = engine.getChessPiece("Pawn", Team.BLACK);
        engine.addPiece(pawn, 6, 4);
        engine.addPiece(engine.getChessPiece(), 5, 4);
        assertTrue(engine.getMoves(pawn).isEmpty());
    }

    @Test
    void testPawnBlockedTwoForwardMove(){
        ChessPiece pawn = engine.getChessPiece("Pawn", Team.BLACK);
        engine.addPiece(pawn, 6, 4);
        engine.addPiece(engine.getChessPiece(), 4, 4);
        ArrayList<ChessBoardMove> expectedMoves = new ArrayList<>();
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(5, 4)));
        assertEquals(expectedMoves, engine.getMoves(pawn));
    }
    @Test
    void testPawnDiagonalMoves() {
        ChessPiece pawn = engine.getChessPiece("Pawn", Team.BLACK);
        engine.addPiece(pawn, 6, 4);
        engine.addPiece(engine.getChessPiece(), 5, 4);
        assertTrue(engine.getMoves(pawn).isEmpty());
        engine.addPiece(engine.getChessPiece("Pawn", Team.WHITE), 5, 3);
        ArrayList<ChessBoardMove> expectedMoves = new ArrayList<>();
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(5, 3)));
        assertEquals(expectedMoves, engine.getMoves(pawn));
        engine.addPiece(engine.getChessPiece("Pawn", Team.WHITE), 5, 5);
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(5, 5)));
        assertEquals(expectedMoves, engine.getMoves(pawn));
    }

}
