package hr.fer.zemris.lystems.impl;

/**
 * Processor class represents a model of an object capable of performing some operation on an object.
 *
 * @author Korina Šimičević
 */
public interface Processor<T> {

    /**
     * This method processes a given object.
     *
     * @param value object to be processed
     */
    void process(T value);
}
