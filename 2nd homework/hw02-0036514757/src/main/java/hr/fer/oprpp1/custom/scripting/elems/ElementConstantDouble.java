package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementConstantDouble extends Element {
    private double value;

    public ElementConstantDouble(double value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementConstantDouble that = (ElementConstantDouble) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
