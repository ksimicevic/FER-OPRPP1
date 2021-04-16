package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

public class CalcLayoutTest {

    @Test
    public void testBounds() {
        JPanel p = new JPanel(new CalcLayout(3));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(-2, 8)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(1, 2)));

        p.add(new JLabel("x"), new RCPosition(1, 6));
        assertThrows(IllegalArgumentException.class, () -> p.add(new JLabel("x"), new RCPosition(1, 6)));
    }
}
