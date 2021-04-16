package hr.fer.zemris.lystems.impl;

public interface List<T> extends Collection<T> {
    T get(int index);

    void insert(T value, int position);

    int indexOf(Object value);

    void remove(int index);
}
