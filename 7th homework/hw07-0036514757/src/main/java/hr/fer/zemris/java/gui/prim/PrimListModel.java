package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.LinkedList;
import java.util.List;

/**
 * PrimListModel is a representation of ListModel. All logic happens here.
 */
public class PrimListModel implements ListModel<Integer> {
    private List<ListDataListener> listeners = new LinkedList<>();
    private List<Integer> elements = new LinkedList<>();
    private Integer current = 1;

    public PrimListModel() {
        elements.add(current);
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return elements.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    /**
     * This method looks for next prime number after the last found one. After it has found a next prime, it will
     * notify all listeners that there is a new number to be shown.
     *
     * @return next prime number
     */
    public Integer next() {
        int candidate = current + 1;
        boolean found = false;

        while (!found) {
            double sqrt = Math.sqrt(candidate);
            found = true;

            for (int i = 2; i <= sqrt; i++) {
                if (candidate % i == 0) {
                    found = false;
                    break;
                }
            }

            if (found) {
                current = candidate;
                elements.add(current);
            } else {
                candidate++;
            }
        }

        for (var listener : listeners) {
            listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getSize(), getSize()));
        }

        return current;
    }
}
