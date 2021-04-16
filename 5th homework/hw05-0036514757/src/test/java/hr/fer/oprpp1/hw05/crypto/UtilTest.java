package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    public void testHextobyte() {
        String hex = "01aE22";
        byte[] bytes = Util.hextobyte(hex);
        assertArrayEquals(new byte[]{1, -82, 34}, bytes);
    }

    @Test
    public void testBytetohex() {
        String hex = Util.bytetohex(new byte[]{1, -82, 34});
        assertEquals("01ae22", hex);
    }
}
