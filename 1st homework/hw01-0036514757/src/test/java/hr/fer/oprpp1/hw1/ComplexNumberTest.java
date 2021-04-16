package hr.fer.oprpp1.hw1;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ComplexNumberTest {

    @Test
    public void testParse() {
        ComplexNumber c1 = new ComplexNumber(2, 3);
        assertEquals(c1, ComplexNumber.parse("2 + 3i"));

        ComplexNumber c2 = new ComplexNumber(2, -3);
        assertEquals(c2, ComplexNumber.parse("2 - 3i"));

        ComplexNumber c3 = new ComplexNumber(0, 3);
        assertEquals(c3, ComplexNumber.parse("3i"));

        ComplexNumber c4 = new ComplexNumber(-2, -3);
        assertEquals(c4, ComplexNumber.parse("-2 - 3i"));

    }

    @Test
    public void testMagnitude() {
        ComplexNumber c1 = new ComplexNumber(3, 4);
        assertEquals(5, c1.getMagnitude());
    }

    @Test
    public void testAngle() {
        ComplexNumber c1 = new ComplexNumber(3, 4);
        BigDecimal angle = BigDecimal.valueOf(c1.getAngle()).round(new MathContext(6));
        if (!angle.equals(new BigDecimal("0.927295"))) {
            fail();
        }
    }

    @Test
    public void testAdd() {
        ComplexNumber c1 = new ComplexNumber(7, 1);
        ComplexNumber c2 = new ComplexNumber(-2, -3);

        assertEquals(new ComplexNumber(5, -2), c1.add(c2));
    }

    @Test
    public void testSub() {
        ComplexNumber c1 = new ComplexNumber(7, 1);
        ComplexNumber c2 = new ComplexNumber(-2, -3);

        assertEquals(new ComplexNumber(9, 4), c1.sub(c2));
    }

    @Test
    public void testMul() {
        ComplexNumber c1 = new ComplexNumber(7, 1);
        ComplexNumber c2 = new ComplexNumber(-2, -3);

        assertEquals(new ComplexNumber(-11, -23), c1.mul(c2));
    }

    @Test
    public void testDiv() {
        ComplexNumber c1 = new ComplexNumber(1, 1);
        ComplexNumber c2 = new ComplexNumber(-1, -1);

        assertEquals(new ComplexNumber(-1, 0), c1.div(c2));
    }

    @Test
    public void testPow() {
        ComplexNumber c1 = new ComplexNumber(7, 1);

        assertEquals(new ComplexNumber(2108, 1344), c1.power(4));
    }

    @Test
    public void testRoot() {
        ComplexNumber c1 = new ComplexNumber(0, 2);
        ComplexNumber[] roots = c1.root(2);

        assertEquals(new ComplexNumber(1, 1), roots[0]);
        assertEquals(new ComplexNumber(-1, -1), roots[1]);
    }

}
