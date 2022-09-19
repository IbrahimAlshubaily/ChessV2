import org.junit.jupiter.api.Test;
import org.pxf.controller.LRUCache;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LRUCacheTest {
    @Test
    void testGeneral() {
        LRUCache lRUCache = new LRUCache(2);

        lRUCache.put(0, 1);
        lRUCache.put(2, 2);
        assertEquals(lRUCache.get(0), 1);

        lRUCache.put(3, 3);
        assertEquals(lRUCache.get(2), -1);

        lRUCache.put(4, 4);
        assertEquals(lRUCache.get(0), -1);
        assertEquals(lRUCache.get(1), -1);
        assertEquals(lRUCache.get(3), 3);
        assertEquals(lRUCache.get(4), 4);
    }


    @Test
    void testCapacityIsOne() {

        LRUCache lRUCache = new LRUCache(1);

        lRUCache.put(2, 1);
        assertEquals(lRUCache.get(2), 1);

        lRUCache.put(3, 2);
        assertEquals(lRUCache.get(2), -1);
        assertEquals(lRUCache.get(3), 2);
    }

    @Test
    void testUpdate(){
        LRUCache lRUCache = new LRUCache(2);

        assertEquals(lRUCache.get(2), -1);

        lRUCache.put(2, 6);
        assertEquals(lRUCache.get(1), -1);

        lRUCache.put(1, 5);
        lRUCache.put(1, 2);
        assertEquals(lRUCache.get(1), 2);

        assertEquals(lRUCache.get(2), 6);
    }

    @Test
    void testUpdateOrder() {
        LRUCache lRUCache = new LRUCache(2);

        lRUCache.put(2, 1);
        lRUCache.put(1, 1);
        lRUCache.put(2, 3);
        lRUCache.put(4, 1);

        assertEquals(lRUCache.get(1), -1);

        assertEquals(lRUCache.get(2), 3);
    }
}

