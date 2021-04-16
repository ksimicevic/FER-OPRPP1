package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

    @Test
    public void testCreateDefault() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        for (int i = 0; i < 16; i++) {
            c.add(i);
        }

        if (c.toArray().length != 16) {
            fail("Size should be 16 and size is " + c.toArray().length);
        }
    }

    @Test
    public void testCreateWithOtherCollectionInitial() {
        int initialSize = 43;
        ArrayIndexedCollection c = new ArrayIndexedCollection(initialSize);
        for (int i = 0; i < initialSize; i++) {
            c.add(i);
        }

        ArrayIndexedCollection c1 = new ArrayIndexedCollection(c);

        if (c1.toArray().length != initialSize) {
            fail("Size should be " + initialSize + " and it's " + c1.toArray().length);
        }
    }

    @Test
    public void testAddFirstElement() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        c.add("Ayy");

        if (!c.contains("Ayy")) {
            fail("Newly added object isn't found in collection.");
        }
    }

    @Test
    public void testAddToFullArrray() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        for (int i = 0; i < 16; i++) {
            c.add(i);
        }
        c.add(17);
        if (c.size() != 17) {
            fail("Size should be 17");
        }
    }

    @Test
    public void testSizeEqualToNoOfElements() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        if (c.size() != size) {
            fail("Size isn't equal to number of elements");
        }
    }

    @Test
    public void testGetIndexOutOfBoundsThrows() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        assertThrows(IndexOutOfBoundsException.class, () -> c.get(10));
    }

    @Test
    public void testIndexOf() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        int indexExp = 7;
        int indexGot = c.indexOf(7);
        assertEquals(indexExp, indexGot);
    }

    @Test
    public void testRemove() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        int indexRem = 5;
        c.remove(indexRem);

        if (c.contains(5)) {
            fail("Array shouldn't contain element 5 after removing it");
        }
    }

    @Test
    public void testInsert() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        c.insert(982, 4);

        if (!(c.contains(982) && c.indexOf(982) == 4)) {
            fail("New element isn't found where it's supposed to be");
        }
    }

    @Test
    public void testGet() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        assertEquals(3, c.get(3));
    }

    @Test
    public void testContains() {
        ArrayIndexedCollection c = new ArrayIndexedCollection();
        int size = 10;

        for (int i = 0; i < size; i++) {
            c.add(i);
        }

        if (!c.contains(0)) {
            fail("Collection should contain 0");
        }

        if (c.contains(10)) {
            fail("Collection shouldn't contain 10");
        }
    }


}
