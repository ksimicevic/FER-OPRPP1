package hr.fer.oprpp1.custom.collections;

/**
 * Processor class represents a model of an object capable of performing some operation on an object.
 *
 * @author Korina Šimičević
 */
public interface Processor {

    /**
     * This method processes a given object.
     *
     * @param value object to be processed
     */
    void process(Object value);
}
