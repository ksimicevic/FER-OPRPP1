package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.*;

import java.util.Arrays;

/**
 * This node represents and saves all contents found in the empty tags in the text.
 */
public class EchoNode extends Node {
    private Element[] elements;

    /**
     * Creates a new echo node.
     *
     * @param elements elements of the echo node
     */
    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EchoNode echoNode = (EchoNode) o;
        return Arrays.equals(elements, echoNode.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }
}
