package hr.fer.zemris.lystems.impl;

/**
 * This class represents an exception that will be thrown when method is called on an empty stack.
 */
public class EmptyStackException extends RuntimeException {

    public EmptyStackException(String message) {
        super(message);
    }
}
