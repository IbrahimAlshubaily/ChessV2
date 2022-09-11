import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pxf.model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    private Engine engine;
    @BeforeEach
    void initTrie(){
        engine = new Engine();
    }

    @Test
    void testEngineInit() {
        assertFalse(engine.isGameOver());
        assertEquals(0, engine.getPiecesCount());

        engine.initPieces();
        assertEquals(32, engine.getPiecesCount());
        assertEquals(16, engine.getPiecesCount("Player 1"));
        assertEquals(16, engine.getPiecesCount("Player 2"));

        engine.initPieces();//should do nothing (board is already initialized)
        assertEquals(32, engine.getPiecesCount());
        assertEquals(16, engine.getPiecesCount("Player 1"));
        assertEquals(16, engine.getPiecesCount("Player 2"));
    }

    @Test
    void testAddPiece() {
        //OUT Of BOUNDS
        ChessPiece testPiece = engine.getChessPiece();
        engine.addPiece(testPiece, -1, 0);
        engine.addPiece(testPiece, 0, -1);
        assertEquals(0, engine.getPiecesCount());

        engine.addPiece(testPiece, 0, 0);
        assertEquals(1, engine.getPiecesCount());

        //Already Occupied
        engine.addPiece(engine.getChessPiece(), 0, 0);
        assertEquals(1, engine.getPiecesCount());

        //Adding the same piece to two different locations
        engine.addPiece(testPiece, 1, 1);
        assertEquals(1, engine.getPiecesCount());
    }

    @Test
    void testPawnMoves(){
        ChessPiece whitePawn = engine.getChessPiece("Pawn", Team.WHITE);
        engine.addPiece(whitePawn, 1, 4);
        ArrayList<ChessBoardMove> actualMoves = engine.getMoves(whitePawn);

        ArrayList<ChessBoardMove> expectedMoves = new ArrayList<>();
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(1, 4), new ChessBoardPosition(2, 4)));
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(1, 4), new ChessBoardPosition(3, 4)));

        assertEquals(expectedMoves.size(), actualMoves.size());
        for (int i = 0; i < actualMoves.size(); i++)
            assertEquals(expectedMoves.get(i), actualMoves.get(i));

        ChessPiece blackPawn =engine.getChessPiece("Pawn", Team.BLACK);
        engine.addPiece(blackPawn, 6, 4);
        actualMoves = engine.getMoves(blackPawn);
        expectedMoves = new ArrayList<>();
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(5, 4)));
        expectedMoves.add(new ChessBoardMove(new ChessBoardPosition(6, 4), new ChessBoardPosition(4, 4)));
        assertEquals(expectedMoves.size(), actualMoves.size());
        for (int i = 0; i < actualMoves.size(); i++)
            assertEquals(expectedMoves.get(i), actualMoves.get(i));
    }


}
