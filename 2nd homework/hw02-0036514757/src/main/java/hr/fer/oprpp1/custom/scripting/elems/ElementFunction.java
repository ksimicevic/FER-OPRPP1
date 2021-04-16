package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementFunction extends Element {
    private String name;

    public ElementFunction(String name) {
        this.name = name;
    }

    @Override
    public String asText() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementFunction that = (ElementFunction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
