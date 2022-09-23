import org.junit.jupiter.api.Test;
import org.pxf.controller.LRUCache;
import org.pxf.model.ChessBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LRUCacheTest {

    @Test
    void testUpdate(){
        LRUCache lRUCache = new LRUCache(2);
        ChessBoard boardOne = new ChessBoard();

        assertEquals(0, lRUCache.get(boardOne));

        lRUCache.put(boardOne, 1);
        assertEquals(1, lRUCache.get(boardOne));

        lRUCache.put(boardOne, 0);
        lRUCache.put(boardOne, 1);
        lRUCache.put(boardOne, 0);
        lRUCache.put(boardOne, 1);
        lRUCache.put(boardOne, 0);
        assertEquals(0.5, lRUCache.get(boardOne));
    }

    @Test
    void testCapacity() {

        LRUCache lRUCache = new LRUCache(2);

        ChessBoard boardOne = new ChessBoard();
        lRUCache.put(boardOne, 1);
        assertEquals(1, lRUCache.get(boardOne));

        ChessBoard boardTwo = new ChessBoard();
        lRUCache.put(boardTwo, 1);
        assertEquals(1, lRUCache.get(boardTwo));

        ChessBoard boardThree = new ChessBoard();
        lRUCache.put(boardThree, 1);
        assertEquals(1, lRUCache.get(boardThree));

        assertEquals(0, lRUCache.get(boardOne));
        assertEquals(1, lRUCache.get(boardTwo));
        assertEquals(1, lRUCache.get(boardThree));

    }


    @Test
    void testUpdateOrder() {
        LRUCache lRUCache = new LRUCache(2);

        ChessBoard boardOne = new ChessBoard();
        lRUCache.put(boardOne, 1);
        assertEquals(1, lRUCache.get(boardOne));

        ChessBoard boardTwo = new ChessBoard();
        lRUCache.put(boardTwo, 1);
        assertEquals(1, lRUCache.get(boardTwo));
        assertEquals(1, lRUCache.get(boardOne));


        ChessBoard boardThree = new ChessBoard();
        lRUCache.put(boardThree, 1);
        assertEquals(1, lRUCache.get(boardThree));
        assertEquals(0, lRUCache.get(boardTwo));
        assertEquals(1, lRUCache.get(boardOne));



        ChessBoard boardFour = new ChessBoard();
        lRUCache.put(boardFour, 1);
        assertEquals(1, lRUCache.get(boardOne));
        assertEquals(1, lRUCache.get(boardFour));


        assertEquals(0, lRUCache.get(boardTwo));
        assertEquals(0, lRUCache.get(boardThree));

    }
}

