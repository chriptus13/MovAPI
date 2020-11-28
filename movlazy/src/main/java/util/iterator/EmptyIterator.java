package util.iterator;

import java.util.NoSuchElementException;

public class EmptyIterator<T> implements java.util.Iterator<T> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }
}
