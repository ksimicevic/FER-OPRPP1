package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    @Test
    public void testCreateDictionary() {
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        dictionary.put(3, "Ay");
        dictionary.put(5, "Nay");
        dictionary.put(89, "Oi");

        assertEquals(dictionary.get(3), "Ay");
        assertEquals(dictionary.get(5), "Nay");
        assertEquals(dictionary.get(89), "Oi");
        assertNull(dictionary.get(21));
    }

    @Test
    public void testRemove() {
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        dictionary.put(3, "Ay");
        dictionary.put(5, "Nay");
        dictionary.put(89, "Oi");

        assertEquals(dictionary.remove(3), "Ay");
        assertNull(dictionary.get(3));
        assertNull(dictionary.remove(43));
    }

    @Test
    public void testPuttingExistingKey() {
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        dictionary.put(3, "Ay");
        dictionary.put(5, "Nay");
        dictionary.put(89, "Oi");

        assertEquals(dictionary.put(3, "Ayaya"), "Ay");
        assertEquals(dictionary.get(3), "Ayaya");
    }

    @Test
    public void testIsEmptyAfterClear(){
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        dictionary.put(3, "Ay");
        dictionary.put(5, "Nay");
        dictionary.put(89, "Oi");

        dictionary.clear();

        assertTrue(dictionary.isEmpty());
    }
}
