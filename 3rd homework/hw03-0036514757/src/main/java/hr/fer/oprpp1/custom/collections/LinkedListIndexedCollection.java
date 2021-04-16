package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This class represents a model of linked list.
 *
 * @author Korina Šimičević
 */
public class LinkedListIndexedCollection<T> implements List<T> {
    private static class ListNode<T> {
        ListNode<T> previous;
        ListNode<T> next;
        T value;
    }

    private ListNode<T> first;
    private ListNode<T> last;
    private int size;
    private long modificationCount = 0;

    /**
     * Creates a new empty list.
     */
    public LinkedListIndexedCollection() {
        first = last = null;
    }

    /**
     * Creates a new list with elements added from given collection.
     * @param collection whose elements will be added to the new list
     */
    public LinkedListIndexedCollection(Collection<? extends T> collection) {
        this();
        this.addAll(collection);
        this.size = collection.size();
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
     * Adds an object to the end of collection.
     *
     * @param value an object to be added to collection
     * @throws NullPointerException if given value is null
     */
    @Override
    public void add(T value) {
        if (value == null) {
            throw new NullPointerException("Cannot add null to the list.");
        }

        ListNode<T> el = new ListNode<>();
        el.next = null;
        el.value = value;

        if (first == null) {
            first = el;
        } else {
            el.previous = this.last;
            last.next = el;
        }
        last = el;

        this.size++;
    }

    /**
     * Returns true if collection contains a given object, otherwise returns false.
     *
     * @param value object that is being looked for in collection
     * @return true if object is present
     */
    @Override
    public boolean contains(Object value) {
        for (var i = first; i != null; i = i.next) {
            if (i.value.equals(value)) {
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

        if (index > 0) {
            this.remove(index);
            return true;
        }

        return false;
    }

    /**
     * Removes an element from list at given index.
     *
     * @param index of element to be removed
     * @throws IndexOutOfBoundsException if index isn't between 0 and size - 1
     */
    public void remove(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("Index should be within range of 0 and size - 1.");
        }

        int j = 0;
        ListNode<T> tmp = new ListNode<>();
        for (var i = first; i != null; i = i.next) {
            if (j == index) {
                tmp = i;
                break;
            }
            j++;
        }

        this.size--;

        if (index == 0) {
            this.first = this.first.next;
            tmp.next = null;
            return;
        } else if (index == this.size) {
            this.last = this.last.previous;
            tmp.previous = null;
            return;
        }

        tmp.previous.next = tmp.next;
        tmp.next.previous = tmp.previous;

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

        int index = 0;
        for (var i = first; i != null; i = i.next) {
            if (i.value.equals(value)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Returns the object found at given index.
     *
     * @param index of object to be returned
     * @return object at given index
     * @throws IndexOutOfBoundsException should the index be outside the range of 0 and size - 1
     */
    public T get(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index should be within range of 0 and size - 1.");
        }

        int currentPosition = 0;
        for (var i = first; i != null; i = i.next) {
            if (currentPosition == index) {
                return i.value;
            }
            currentPosition++;
        }

        return null;
    }

    /**
     * Creates a new array with size equal of collection, fills it with objects contained in it and returns the new array.
     *
     * @return array created by copying elements from original collection
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];

        int index = 0;
        for (var i = first; i != null; i = i.next) {
            array[index] = i.value;
            index++;
        }

        return array;
    }

    /**
     * Removes all objects from collection.
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
        this.modificationCount++;
    }

    /**
     * Inserts (but doesn't overwrite) given value at given position in the list if position is valid.
     *
     * @param value    to be inserted into list
     * @param position at which value will be inserted
     * @throws IndexOutOfBoundsException if position isn't within range of 0 and size
     */
    public void insert(T value, int position) {
        if (position < 0 || position > this.size) {
            throw new IndexOutOfBoundsException("Position at which new value will be placed needs to be within range of 0 and size");
        }

        if (position == this.size) {
            this.add(value);
            return;
        }

        ListNode<T> tmp = new ListNode<>();
        tmp.value = value;

        if (position == 0) {
            tmp.next = first;
            first = tmp;
        } else {
            int index = 0;
            ListNode<T> prev = null;
            ListNode<T> next = null;
            for (var i = first; i != null; i = i.next) {
                if (index == position) {
                    prev = i.previous;
                    next = i;
                    break;
                }
                index++;
            }
            assert prev != null;
            prev.next = tmp;
            next.previous = tmp;

            tmp.previous = prev;
            tmp.next = next;
        }

        this.modificationCount++;
    }

    /**
     * Creates and returns new elements getter that will be used to retrieve elements from collection.
     *
     * @return new elements getter
     */
    public ElementsGetter<T> createElementsGetter() {
        return new ListElementsGetter<>(this);
    }

    /**
     * Class represents an object whose job is to return next element from collection until there are no elements left.
     */
    private static class ListElementsGetter<T> implements ElementsGetter<T> {
        private ListNode<T> current;
        private final ListNode<T> last;
        private final LinkedListIndexedCollection<T> collection;
        private final long savedModificationCount;

        public ListElementsGetter(LinkedListIndexedCollection<T> collection) {
            this.collection = collection;
            this.current = collection.first;
            this.last = collection.last;
            this.savedModificationCount = collection.modificationCount;
        }

        /**
         * Returns true if there are elements to return.
         *
         * @return true if there are more elements
         */
        public boolean hasNextElement() {
            return current != last.next;
        }

        /**
         * Returns the next element in collection.
         *
         * @return next element
         * @throws NoSuchElementException if there are no more elements to return
         */
        public T getNextElement() {
            if(this.savedModificationCount != collection.modificationCount) {
                throw new ConcurrentModificationException("Collection has been modified.");
            }

            if (!hasNextElement()) {
                throw new NoSuchElementException("There are no more elements.");
            }

            T element = current.value;
            current = current.next;
            return element;
        }
    }


}
