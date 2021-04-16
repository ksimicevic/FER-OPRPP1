package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * This class represents a single node in the element tree.
 */
public class Node {
    private ArrayIndexedCollection col;

    /**
     * Adds a child to this node.
     *
     * @param child node to be added as a child to the current nod
     */
    public void addChildNode(Node child) {
        if (this.col == null) {
            this.col = new ArrayIndexedCollection();
        }

        this.col.add(child);
    }

    /**
     * Returns the number of children that current node has.
     *
     * @return number of children
     */
    public int numberOfChildren() {
        return this.col.size();
    }

    /**
     * Returns the child of this node at given index.
     *
     * @param index of child to be returned
     * @return child of current node at given index
     */
    public Node getChild(int index) {
        return (Node) this.col.get(index);
    }

    /**
     * Returns the collection that is used to store the child nodes.
     *
     * @return collection consisting of child nodes
     */
    public ArrayIndexedCollection getCol() {
        return col;
    }
}
