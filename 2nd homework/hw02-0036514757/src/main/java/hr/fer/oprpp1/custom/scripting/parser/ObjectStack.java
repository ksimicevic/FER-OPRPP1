package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * This class represents a model of stack, LIFO data structure.
 *
 * @author Korina Šimičević
 */
public class ObjectStack {
    ArrayIndexedCollection storage;

    /**
     * Creates a new empty stack.
     */
    public ObjectStack() {
        this.storage = new ArrayIndexedCollection();
    }

    /**
     * Returns true if the stack has no elements.
     *
     * @return true if stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.storage.isEmpty();
    }

    /**
     * Returns the number of currently stored elements.
     *
     * @return size of stack
     */
    public int size() {
        return this.storage.size();
    }

    /**
     * Adds a new value to the top of the stack.
     *
     * @param value to be pushed
     */
    public void push(Object value) {
        this.storage.add(value);
    }

    /**
     * Returns and removes the top element on stack.
     *
     * @return element to be removed from stack
     * @throws EmptyStackException if called on an empty stack
     */
    public Object pop() {
        if (this.isEmpty()) {
            throw new EmptyStackException("Cannot pop from an empty stack.");
        }

        int indexOfLast = this.size() - 1;
        Object value = this.storage.get(indexOfLast);
        this.storage.remove(indexOfLast);
        return value;
    }

    /**
     * Returns the top element on stack.
     *
     * @return the top element
     * @throws EmptyStackException if called on an empty stack
     */
    public Object peek() {
        if (this.isEmpty()) {
            throw new EmptyStackException("Cannot peek because the stack is empty.");
        }

        return this.storage.get(this.size() - 1);
    }

    /**
     * Removes all elements from stack.
     */
    public void clear() {
        this.storage.clear();
    }
}
