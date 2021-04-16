package hr.fer.zemris.lystems.impl;

import java.util.Objects;

public class Dictionary<K, V> {
    public static class DictionaryEntry<K, V> {
        private K key;
        private V value;

        /**
         * Creates a new dictionary entry consisting of a key and a value.
         *
         * @param key   key in entry
         * @param value value in entry
         * @throws NullPointerException in case given key is null
         */
        public DictionaryEntry(K key, V value) {
            if (key == null) {
                throw new NullPointerException("Key cannot be null");
            }

            this.key = key;
            this.value = value;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DictionaryEntry<?, ?> that = (DictionaryEntry<?, ?>) o;
            return key.equals(that.key) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private ArrayIndexedCollection<DictionaryEntry<K, V>> collection;

    public Dictionary() {
        this.collection = new ArrayIndexedCollection<>();
    }

    /**
     * Returns true if the collection is empty, otherwise returns false.
     *
     * @return true if collection contains no objects
     */
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    /**
     * Returns the number of objects in dictionary.
     *
     * @return size of dictionary
     */
    public int size() {
        return this.collection.size();
    }

    /**
     * Removes all objects from collection.
     */
    public void clear() {
        this.collection.clear();
    }

    /**
     * Puts a new DictionaryEntry into the Dictionary. If there's already an entry with the same key as the given one,
     * the method will place new value into the entry and return the old one. If there was no entry with given key, method will create
     * a new DictionaryEntry and return null.
     *
     * @param key   key of new entry
     * @param value value of new entry
     * @return oldValue if there's already an entry with given key, otherwise null
     * @throws NullPointerException if given key is null
     */
    public V put(K key, V value) {
        if(key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        var getter = this.collection.createElementsGetter();

        while (getter.hasNextElement()) {
            var currentEntry = getter.getNextElement();
            if (currentEntry.getKey().equals(key)) {
                var oldValue = currentEntry.getValue();
                currentEntry.setValue(value);
                return oldValue;
            }
        }

        this.collection.add(new DictionaryEntry<>(key, value));
        return null;
    }

    /**
     * Returns the value of DictionaryEntry if the given key exists in the collection, otherwise returns null.
     *
     * @param key key for which the method will attempt to find the corresponding value
     * @return value for given key, if not found, returns null
     * @throws NullPointerException if given key is null
     */
    public V get(K key) {
        if(key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        var getter = this.collection.createElementsGetter();

        while (getter.hasNextElement()) {
            var currentEntry = getter.getNextElement();
            if (currentEntry.getKey().equals(key)) {
                return currentEntry.getValue();
            }
        }

        return null;
    }

    /**
     * Removes DictionaryEntry for given key, if such exists. If method successfully finds the entry and removes it, it
     * will return the oldValue, else it'll return null.
     *
     * @param key key of entry that the method will attempt to remove from Dictionary
     * @return oldValue if method successfully removed the entry, otherwise null
     * @throws NullPointerException if given key is null
     */
    public V remove(K key) {
        if(key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        var getter = this.collection.createElementsGetter();
        V oldValue = null;
        boolean found = false;

        while (getter.hasNextElement()) {
            var currentEntry = getter.getNextElement();
            if (currentEntry.getKey().equals(key)) {
                found = true;
                oldValue = currentEntry.getValue();
            }
        }

        if (found) {
            this.collection.remove(new DictionaryEntry<>(key, oldValue));
            return oldValue;
        } else {
            return null;
        }
    }


}
