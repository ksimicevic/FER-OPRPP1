package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    @Test
    public void testCreateListFromOtherCollection() {
        LinkedListIndexedCollection c = new LinkedListIndexedCollection();
        for (int i = 0; i < 10; i++) {
            c.add(i);
        }
        LinkedListIndexedCollection l = new LinkedListIndexedCollection(c);
        if (l.size() != 10) {
            fail("Size should be 10");
        }
    }

    @Test
    public void testAddingElementsToList() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        if (l.size() != 10) {
            fail("Size should be 10");
        }
    }

    @Test
    public void testContains() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        for (int i = 0; i < 10; i++) {
            if (!l.contains(i)) {
                fail("The list should contain all elements.");
            }
        }

        if (l.contains(11)) {
            fail("Contains should return false because the list doesn't contain 11");
        }
    }

    @Test
    public void testRemoveAtIndex() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        //Test removing first element, first element is at index 0 and is 0
        l.remove(0);
        if (l.size() != 9) {
            fail("Size isn't 9 when removing one element from list with size of 10");
        }
        if (l.contains(0)) {
            fail("List shouldn't contain 0 after removing it.");
        }

        //Test removing middle element, at index 3 after removing first element will be 4
        l.remove(3);
        if (l.size() != 8) {
            fail("Size isn't 8 when removing one element from list with size of 9");
        }
        if (l.contains(4)) {
            fail("List shouldn't contain 4 after removing it.");
        }

        //Test removing last element, at index 7 after removing 2 elements
        l.remove(7);
        if (l.size() != 7) {
            fail("Size isn't 7 when removing one element from list with size of 8");
        }
        if (l.contains(10)) {
            fail("List shouldn't contain 10 after removing it.");
        }

        //Testing IndexOutOfBounds exception
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(7));

    }

    @Test
    public void testInsert() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        //Insert at the beginning
        l.insert(45, 0);
        if (!l.get(0).equals(45)) {
            fail("Expected 45 as first element.");
        }

        //Insert in the middle
        l.insert(32, 5);
        if (!l.get(5).equals(32)) {
            fail("Expected 32 at index 5.");
        }

        //Insert at the end
        l.insert(65, l.size());
        if (!l.get(l.size() - 1).equals(65)) {
            fail("Expected 65 at the end.");
        }
    }

    @Test
    public void testRemoveObject() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add("String " + i);
        }

        l.remove("String 5");

        if (l.contains("String 5")) {
            fail("List shouldn't contain removed element");
        }
    }

    @Test
    public void testToArray() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        Object[] array = l.toArray();

        for (int i = 0; i < 10; i++) {
            if (!l.get(i).equals(array[i])) {
                fail("All items from list should be in the array");
            }
        }
    }

    @Test
    public void testIndexOf() {
        LinkedListIndexedCollection l = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        if (l.indexOf(5) != 5) {
            fail("Index of 5 should be 5");
        }
    }


}
