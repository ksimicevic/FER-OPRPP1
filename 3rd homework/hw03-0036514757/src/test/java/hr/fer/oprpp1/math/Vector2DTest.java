package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {
    @Test
    public void testAdd() {
        Vector2D v = new Vector2D(3.4, 1.2);
        Vector2D o = new Vector2D(1.0, 2.0);

        v.add(o);

        assertTrue(Double.compare(v.getX(), 4.4) == 0);
        assertTrue(Double.compare(v.getY(), 3.2) == 0);
    }

    @Test
    public void testAdded() {
        Vector2D v = new Vector2D(3.4, 1.2);
        Vector2D o = new Vector2D(1.0, 2.0);

        Vector2D r = v.added(o);

        assertTrue(Double.compare(v.getX(), 3.4) == 0);
        assertTrue(Double.compare(v.getY(), 1.2) == 0);

        assertTrue(Double.compare(r.getX(), 4.4) == 0);
        assertTrue(Double.compare(r.getY(), 3.2) == 0);
    }

    @Test
    public void testScale() {
        Vector2D v = new Vector2D(3.4, 1.2);

        v.scale(2.0);

        assertTrue(Double.compare(v.getX(), 6.8) == 0);
        assertTrue(Double.compare(v.getY(), 2.4) == 0);
    }

    @Test
    public void testScaled() {
        Vector2D v = new Vector2D(3.4, 1.2);

        Vector2D r = v.scaled(2.0);

        assertTrue(Double.compare(v.getX(), 3.4) == 0);
        assertTrue(Double.compare(v.getY(), 1.2) == 0);

        assertTrue(Double.compare(r.getX(), 6.8) == 0);
        assertTrue(Double.compare(r.getY(), 2.4) == 0);
    }

    @Test
    public void testCopy() {
        Vector2D v = new Vector2D(3.4, 1.2);
        Vector2D c = v.copy();

        assertEquals(v.getX(), c.getX());
        assertEquals(v.getY(), c.getY());
    }

    @Test
    public void testRotate() {
        Vector2D v = new Vector2D(2.2, 0);
        v.rotate(Math.PI / 2);

        assertTrue(Math.abs(v.getX()) < 1E-8);
        assertTrue(2.2 - Math.abs(v.getY()) < 1E-8);
    }

    @Test
    public void testVectorRotated() {
        Vector2D v = new Vector2D(2.2, 2.2);
        Vector2D c = v.rotated(Math.PI);

        assertTrue(v.getX() == 2.2);
        assertTrue(v.getY() == 2.2);

        assertTrue(2.2 - Math.abs(v.getX()) < 1E-8);
        assertTrue(2.2 - Math.abs(v.getY()) < 1E-8);
    }

}
