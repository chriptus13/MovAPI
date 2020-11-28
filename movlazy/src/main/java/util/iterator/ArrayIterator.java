package util.iterator;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
    final T[] values;
    int index = 0;
    public ArrayIterator(T[] values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        return index < values.length;
    }

    @Override
    public T next() {
        return values[index++];
    }
}
