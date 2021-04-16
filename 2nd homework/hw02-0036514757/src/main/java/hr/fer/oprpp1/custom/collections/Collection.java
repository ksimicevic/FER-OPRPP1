package hr.fer.oprpp1.custom.collections;

/**
 * Collection interface represents a model of a general collection of objects.
 *
 * @author Korina Šimičević
 */
public interface Collection {

    /**
     * Returns true if the collection is empty, otherwise returns false.
     *
     * @return true if collection contains no objects
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of objects in collection.
     *
     * @return size of collection
     */
    int size();

    /**
     * Adds an object to collection.
     *
     * @param value an object to be added to collection
     */
    void add(Object value);

    /**
     * Returns true if collection contains a given object, otherwise returns false.
     *
     * @param value object that is being looked for in collection
     * @return true if object is present
     */
    boolean contains(Object value);

    /**
     * Removes one instance of given object from the collection if it exists.
     *
     * @param value object to be removed
     * @return true if object is successfully found and removed
     */
    boolean remove(Object value);

    /**
     * Creates a new array with size equal of collection, fills it with objects contained in it and returns the new array.
     *
     * @return array created by copying elements from original collection
     */
    Object[] toArray();

    /**
     * Calls the process() method from given processor and processes each object from collection.
     *
     * @param processor that will process all objects in collection
     */
    default void forEach(Processor processor) {
        ElementsGetter getter = this.createElementsGetter();

        while(getter.hasNextElement()) {
            processor.process(getter.getNextElement());
        }
    }

    /**
     * Adds all elements of given collection to this collection. Given collection will remain unchanged.
     *
     * @param other collection whose elements will be added to this collection
     */
    default void addAll(Collection other) {
        other.forEach(Collection.this::add);
    }

    /**
     * Removes all objects from collection.
     */
    void clear();

    /**
     * Creates and returns a new elements getter that will be used to get elements from collections.
     *
     * @return new elements getter
     */
    ElementsGetter createElementsGetter();

    /**
     * Methods that every elements getter needs to implement.
     */
    interface ElementsGetter {
        boolean hasNextElement();

        Object getNextElement();

        default void processRemaining(Processor p) {
            while (hasNextElement()) {
                p.process(getNextElement());
            }
        }
    }

    default void addAllSatisfying(Collection col, Tester tester) {
        ElementsGetter getter = col.createElementsGetter();

        while(getter.hasNextElement()) {
            Object value = getter.getNextElement();

            if(tester.test(value)) {
                this.add(value);
            }
        }
    }
}
