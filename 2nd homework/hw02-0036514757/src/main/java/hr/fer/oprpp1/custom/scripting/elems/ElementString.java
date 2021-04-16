package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementString extends Element {
    private String value;

    public ElementString(String value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementString that = (ElementString) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
