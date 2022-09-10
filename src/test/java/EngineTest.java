import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pxf.model.ChessPiece;
import org.pxf.model.Engine;

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
        assertEquals("Player 1", engine.getTurn());
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


}
