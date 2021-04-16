package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.ElementString;

import java.util.Objects;

/**
 * Text node represents a node which contains text.
 */
public class TextNode extends Node {
    private ElementString string;

    /**
     * Creates a new text node.
     * @param string text(string) in the node
     */
    public TextNode(ElementString string) {
        this.string = string;
    }

    /**
     * Returns the text (string) contained in the node.
     *
     * @return string contained in the nod
     */
    public ElementString getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextNode textNode = (TextNode) o;
        return Objects.equals(string, textNode.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}
