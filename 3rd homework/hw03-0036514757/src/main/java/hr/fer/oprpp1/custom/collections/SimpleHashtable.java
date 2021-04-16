package hr.fer.oprpp1.custom.collections;

import java.util.*;

/**
 * This class represents a model of simple hash map.
 *
 * @param <K> parameter of key
 * @param <V> parameter of value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
    private static final int DEFAULT_SIZE = 16;

    /**
     * Private nested class representing a single (key, value) entry in hash table.
     * Each entry also has a reference pointing to next entry in the list of entries.
     *
     * @param <K> parameter of key
     * @param <V> parameter of value
     */
    protected static class TableEntry<K, V> {
        private K key;
        private V value;
        TableEntry<K, V> next;

        /**
         * Creates a new entry consisting of key and value.
         *
         * @param key   of entry, cannot be null
         * @param value of entry, can be null
         * @throws NullPointerException in case given key is null
         */
        public TableEntry(K key, V value) {
            if (key == null) {
                throw new NullPointerException("Key cannot be null");
            }

            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    private TableEntry<K, V>[] table;
    private int size;
    private int modificationCount = 0;

    /**
     * Creates a new simple hash table with default size. Default size is set to 16.
     */
    public SimpleHashtable() {
        this(DEFAULT_SIZE);
    }

    /**
     * Creates a new simple hash table with initial capacity. In case that given initial capacity is not a number
     * that is power of 2, the initial capacity will be set to the first nearest such number.
     *
     * @param initialCapacity of new hash table
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Size cannot be less than 1");
        }

        this.size = 0;

        if ((initialCapacity & (initialCapacity - 1)) == 0) {
            table = (TableEntry<K, V>[]) new TableEntry[initialCapacity];
        } else {
            int capacity = (int) Math.pow(2.0, Math.floor(Math.log10(initialCapacity) / Math.log10(2) + 1));
            table = (TableEntry<K, V>[]) new TableEntry[capacity];
        }
    }

    /**
     * Puts a new entry into the hash table. If there's already an entry with the same key as the given one,
     * the method will place new value into the entry and return the old one. If there was no entry with given key, method will create
     * a new entry and return null.
     *
     * @param key   key of new entry
     * @param value value of new entry
     * @return oldValue if there's already an entry with given key, otherwise null
     * @throws NullPointerException if given key is null
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        if ((double) this.size / this.table.length >= 0.75) {
            this.reallocate();
        }

        int hash = Math.abs(key.hashCode()) % this.table.length;
        V oldValue = null;

        if (this.table[hash] != null) {
            var i = this.table[hash];
            for (; i.next != null; i = i.next) {
                if (i.key.equals(key)) {
                    oldValue = i.value;
                    i.value = value;
                    break;
                }
            }

            if (oldValue == null) {
                this.modificationCount++;
                i.next = new TableEntry<>(key, value);
                this.size++;
            }

        } else {
            this.size++;
            this.modificationCount++;
            this.table[hash] = new TableEntry<>(key, value);
        }

        return oldValue;
    }

    /**
     * Returns the value of entry if the given key exists in the collection, otherwise returns null.
     *
     * @param key key for which the method will attempt to find the corresponding value
     * @return value for given key, if not found, returns null
     * @throws NullPointerException if given key is null
     */
    public V get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = Math.abs(key.hashCode()) % this.table.length;

        if (this.table[hash] != null) {
            for (var i = this.table[hash]; i != null; i = i.next) {
                if (i.key.equals(key)) {
                    return i.value;
                }
            }
        }

        return null;
    }

    /**
     * Returns true if this hash table has an entry with given key.
     *
     * @param key to be found in this hash table
     * @return true if there's an entry with this key
     * @throws NullPointerException if given key is null
     */
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = Math.abs(key.hashCode()) % this.table.length;

        if (this.table[hash] != null) {
            for (var i = this.table[hash]; i != null; i = i.next) {
                if (i.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if this hash table has at least one entry with given value.
     *
     * @param value to be found in this hash table
     * @return true if value is found
     */
    public boolean containsValue(Object value) {
        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i] != null) {
                for (var j = this.table[i]; j != null; j = j.next) {
                    if (Objects.equals(j.value, value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Removes an entry for given key, if such exists. If method successfully finds the entry and removes it, it
     * will return the oldValue, else it'll return null.
     *
     * @param key key of entry that the method will attempt to remove from simple hash table
     * @return oldValue if method successfully removed the entry, otherwise null
     * @throws NullPointerException if given key is null
     */
    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = Math.abs(key.hashCode()) % this.table.length;
        V oldValue;

        if (this.table[hash] != null) {
            for (var i = this.table[hash]; i != null; i = i.next) {
                if (i.key.equals(key)) {
                    //This will only execute when the first element in the list has this key
                    oldValue = i.value;
                    this.table[hash] = i.next;
                    this.size--;
                    this.modificationCount++;
                    return oldValue;
                } else if (i.next.key.equals(key)) {
                    //This will execute when middle element in the list has this key
                    oldValue = i.next.value;
                    i.next = i.next.next;
                    this.size--;
                    this.modificationCount++;
                    return oldValue;
                }
            }
        }
        return null;
    }

    /**
     * Returns the number of currently stored elements in this hash table.
     *
     * @return size of hash table
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns true if there are no elements in this hash table.
     *
     * @return true if hash table is empty
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns the string representation of this hash table.
     *
     * @return string form of hash table
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i] != null) {
                for (var j = this.table[i]; j != null; j = j.next) {
                    sb.append(j.key.toString()).append("=");
                    if (j.value == null) {
                        sb.append("null");
                    } else {
                        sb.append(j.value.toString());
                    }
                    sb.append(", ");
                }
            }
        }

        String s = sb.toString();
        return s.substring(0, s.length() - 2) + "]";
    }

    /**
     * Creates and returns a new array consisting of all elements that are currently stores in this hash table.
     *
     * @return array consisting of all elements stored in this hash table
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[this.size];

        int index = 0;

        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i] != null) {
                for (var j = this.table[i]; j != null; j = j.next) {
                    array[index++] = j;
                }
            }
        }

        return array;

    }

    /**
     * Deletes the contents in this hash table.
     */
    public void clear() {
        Arrays.fill(this.table, null);
        this.size = 0;
    }

    /**
     * Tables used for storing entries doubles and all the elements are added again into the table.
     */
    @SuppressWarnings("unchecked")
    private void reallocate() {
        TableEntry<K, V>[] array = this.toArray();
        int newSize = this.table.length * 2;
        this.clear();
        this.table = (TableEntry<K, V>[]) new TableEntry[newSize];
        for (var entry : array) {
            this.put(entry.key, entry.value);
        }
    }

    /**
     * Returns iterator for this collection.
     *
     * @return iterator
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl(this);
    }

    /**
     * This class represents a model of iterator. An object that will visit and return each and every element
     * from this collection.
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
        private SimpleHashtable<K, V> hashTable;
        private TableEntry<K, V> currentEntry = null;
        private int currentTableIndex = 0;
        private int currentElementIndex = -1;
        private boolean removeCalled = true;
        private int savedModificationCount;

        public IteratorImpl(SimpleHashtable<K, V> hashTable) {
            this.hashTable = hashTable;
            this.savedModificationCount = hashTable.modificationCount;
        }

        /**
         * Returns true if there are any elements left for iterator to return.
         *
         * @return true if there are any elements remaining
         * @throws ConcurrentModificationException if collection has been modified between two calls
         */
        @Override
        public boolean hasNext() {
            if (savedModificationCount != hashTable.modificationCount) {
                throw new ConcurrentModificationException("Collection has been modified");
            }

            return currentElementIndex != hashTable.size() - 1;
        }

        /**
         * Returns the next element in collection.
         *
         * @return the next element
         * @throws NoSuchElementException in case there aren't any more elements to return
         */
        @Override
        public TableEntry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more elements in collection");
            }

            removeCalled = false;

            if (currentEntry == null || currentEntry.next == null) {
                for (int i = currentTableIndex; i < hashTable.table.length; i++) {
                    if (hashTable.table[i] != null) {
                        currentEntry = hashTable.table[i];
                        currentElementIndex++;
                        currentTableIndex++;
                        break;
                    }
                    currentTableIndex++;
                }
            } else {
                currentEntry = currentEntry.next;
                currentElementIndex++;
            }

            return currentEntry;
        }

        /**
         * Removes from the collection the most recent element from received from next() method.
         * This method can be called once per next() call.
         *
         * @throws IllegalStateException if method is called more than once per next() method call or if iterator hasn't
         *                               returned any elements yet
         */
        @Override
        public void remove() {
            if (removeCalled) {
                throw new IllegalStateException("The remove method cannot be called right now");
            }

            if (hashTable.remove(currentEntry.getKey()) != null) {
                removeCalled = true;
                this.savedModificationCount++;
                this.currentElementIndex--;
            }

        }
    }
}
