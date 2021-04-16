package hr.fer.oprpp1.custom.collections;

/**
 * This class represents a model of linked list.
 *
 * @author Korina Šimičević
 */
public class LinkedListIndexedCollection extends Collection {
    private static class ListNode {
        ListNode previous;
        ListNode next;
        Object value;
    }

    private ListNode first;
    private ListNode last;
    private int size;

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
    public LinkedListIndexedCollection(Collection collection) {
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
    public void add(Object value) {
        if (value == null) {
            throw new NullPointerException("Cannot add null to the list.");
        }

        ListNode el = new ListNode();
        el.next = null;
        el.value = value;

        if (first == null) {
            first = el;
            last = el;
        } else {
            el.previous = this.last;
            last.next = el;
            last = el;
        }

        size++;
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
        ListNode tmp = new ListNode();
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
    public Object get(int index) {
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
     * Calls the process() method from given processor and processes each object from collection.
     *
     * @param processor that will process all objects in collection
     */
    @Override
    public void forEach(Processor processor) {
        for (var i = first; i != null; i = i.next) {
            processor.process(i.value);
        }
    }

    /**
     * Removes all objects from collection.
     */
    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Inserts (but doesn't overwrite) given value at given position in the list if position is valid.
     *
     * @param value    to be inserted into list
     * @param position at which value will be inserted
     * @throws IndexOutOfBoundsException if position isn't within range of 0 and size
     */
    public void insert(Object value, int position) {
        if (position < 0 || position > this.size) {
            throw new IndexOutOfBoundsException("Position at which new value will be placed needs to be within range of 0 and size");
        }

        if (position == this.size) {
            this.add(value);
        }

        ListNode tmp = new ListNode();
        tmp.value = value;

        if (position == 0) {
            tmp.next = first;
            first = tmp;
        } else {
            int index = 0;
            ListNode prev = null;
            ListNode next = null;
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
    }

}
