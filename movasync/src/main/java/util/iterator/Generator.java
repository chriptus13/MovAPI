package util.iterator;

import java.util.Iterator;
import java.util.function.Supplier;

public class Generator<T> implements Iterator<T> {
    final Supplier<T> src;

    public Generator(Supplier<T> src) {
        this.src = src;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        return src.get();
    }
}