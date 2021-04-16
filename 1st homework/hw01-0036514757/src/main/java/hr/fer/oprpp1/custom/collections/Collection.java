package hr.fer.oprpp1.custom.collections;

/**
 * Collection class represents a model of a general collection of objects.
 *
 * @author Korina Šimičević
 */
public class Collection {

    /**
     * Creates an instance of collection.
     */
    protected Collection() {

    }

    /**
     * Returns true if the collection is empty, otherwise returns false.
     *
     * @return true if collection contains no objects
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of objects in collection.
     *
     * @return size of collection
     */
    public int size() {
        return 0;
    }

    /**
     * Adds an object to collection.
     *
     * @param value an object to be added to collection
     */
    public void add(Object value) {

    }

    /**
     * Returns true if collection contains a given object, otherwise returns false.
     *
     * @param value object that is being looked for in collection
     * @return true if object is present
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Removes one instance of given object from the collection if it exists.
     *
     * @param value object to be removed
     * @return true if object is successfully found and removed
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Creates a new array with size equal of collection, fills it with objects contained in it and returns the new array.
     *
     * @return array created by copying elements from original collection
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException("Currently nonoperational");
    }

    /**
     * Calls the process() method from given processor and processes each object from collection.
     *
     * @param processor that will process all objects in collection
     */
    public void forEach(Processor processor) {

    }

    /**
     * Adds all elements of given collection to this collection. Given collection will remain unchanged.
     *
     * @param other collection whose elements will be added to this collection
     */
    public void addAll(Collection other) {
        class InnerProcessor extends Processor {
            public void process(Object value) {
                Collection.this.add(value);
            }
        }

        other.forEach(new InnerProcessor());
    }

    /**
     * Removes all objects from collection.
     */
    public void clear() {

    }
}
