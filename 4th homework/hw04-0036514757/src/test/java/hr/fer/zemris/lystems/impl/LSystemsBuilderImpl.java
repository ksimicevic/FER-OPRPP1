package hr.fer.zemris.lystems.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LSystemsBuilderImpl {
    @Test
    public void testGenerate() {
        LSystemBuilderImpl l = new LSystemBuilderImpl();
        l.registerProduction('F', "F+F--F+F");
        l.setAxiom("F");

        assertEquals("F", l.build().generate(0));
        assertEquals("F+F--F+F", l.build().generate(1));
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", l.build().generate(2));
    }

}
