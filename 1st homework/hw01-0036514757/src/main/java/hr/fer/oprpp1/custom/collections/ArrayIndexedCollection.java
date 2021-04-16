package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

/**
 * This class represents a model of an array.
 *
 * @author Korina Šimičević
 */
public class ArrayIndexedCollection extends Collection {

    private static final int DEFAULT_CAPACITY = 16;

    private int size = 0;
    private Object[] elements;

    /**
     * Creates an array with default size of 16.
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an array with initial capacity.
     *
     * @param initialCapacity size of array to be created
     * @throws IllegalArgumentException if initial capacity is less than 1
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("The size of array cannot be less than 1.");
        }

        this.elements = new Object[initialCapacity];
    }

    /**
     * This method takes another collection and initial size and makes a new collection with all the elements from given collection with
     * initial size. Should the given initial size be smaller than the size of given collection, the size of new collection will be equal
     * to the size of the given collection.
     *
     * @param other           collection whose elements are to be copied to new collection
     * @param initialCapacity initial size of the new collection
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        if (other == null) {
            throw new NullPointerException("Collection whose elements are to be copied cannot be null.");
        }

        this.size = Math.max(initialCapacity, other.size());
        this.elements = Arrays.copyOf(other.toArray(), this.size);
    }

    public ArrayIndexedCollection(Collection other) {
        this(other, DEFAULT_CAPACITY);
    }

    /**
     * Returns the number of objects in collection.
     *
     * @return size of collection
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds an object to collection. Should the collection be full, it'll double it's size and add the element.
     *
     * @param value an object to be added to collection
     * @throws NullPointerException if given value is null
     */
    @Override
    public void add(Object value) {
        if (value == null) {
            throw new NullPointerException("Elements in collection cannot be null.");
        }

        if (this.size >= this.elements.length) {
            this.elements = Arrays.copyOf(this.elements, this.size * 2);
        }

        this.elements[this.size] = value;
        this.size++;
    }

    /**
     * Returns the object found at given index.
     *
     * @param index of object to be returned
     * @return object at given index
     * @throws IndexOutOfBoundsException should the index be outside the range of 0 and size - 1
     */
    public Object get(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index should be within range of 0 and size - 1.");
        }

        return this.elements[index];
    }


    /**
     * Returns true if collection contains a given object, otherwise returns false.
     *
     * @param value object that is being looked for in collection
     * @return true if object is present
     */
    @Override
    public boolean contains(Object value) {
        if (value == null) {
            return false;
        }

        for (int i = 0; i < this.size; i++) {
            if (value.equals(this.elements[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes one instance of given object from the collection if it exists.
     *
     * @param value object to be removed
     * @return true if object is successfully found and removed
     */
    @Override
    public boolean remove(Object value) {
        int index = this.indexOf(value);

        if (index >= 0) {
            this.remove(index);
            return true;
        }

        return false;
    }

    /**
     * Removes an element at given index if it's within bounds.
     *
     * @param index of element to be removed from collection
     * @throws IndexOutOfBoundsException if index is less than 0 and greater than size - 1
     */
    public void remove(int index) {
        if (index < 0 || index > this.size() - 1) {
            throw new IndexOutOfBoundsException("Index of element to be removed should be within range of 0 and size of collection - 1.");
        }

        if (index == this.size - 1) {
            this.size--;
            this.elements[index] = null;
            return;
        }

        for (int i = index; i < this.size; i++) {
            this.elements[i] = this.elements[i + 1];
        }

        this.elements[this.size - 1] = null;
        this.size--;
    }

    /**
     * Creates a new array with size equal of collection, fills it with objects contained in it and returns the new array.
     *
     * @return array created by copying elements from original collection
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.elements, this.size);
    }

    /**
     * Calls the process() method from given processor and processes each object from collection.
     *
     * @param processor that will process all objects in collection
     */
    @Override
    public void forEach(Processor processor) {
        for(int i = 0; i < this.size; i++) {
            processor.process(this.elements[i]);
        }
    }

    /**
     * Removes all objects from collection.
     */
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.elements, null);
    }

    /**
     * Inserts (but doesn't overwrite) given value at given position in the array if position is valid.
     *
     * @param value    to be inserted into array
     * @param position at which value will be inserted
     * @throws IndexOutOfBoundsException if position isn't within range of 0 and size
     */
    public void insert(Object value, int position) {
        if (position < 0 || position > this.size) {
            throw new IndexOutOfBoundsException("Index of element to be inserted should be within range of 0 and size of collection");
        }

        this.add(value);
        for (int i = this.size - 2; i > position - 1; i--) {
            this.elements[i + 1] = this.elements[i];
        }
        this.elements[position] = value;
        this.modificationCount++;

    }

    /**
     * Searches the collection and returns the index of first occurrence of given value or -1 if value isn't found.
     *
     * @param value value to be found in the collection
     * @return index of first occurrence of given value or -1 if value isn't found
     */
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

}
