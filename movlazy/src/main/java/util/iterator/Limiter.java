package util.iterator;

import java.util.Iterator;

public class Limiter<T> implements Iterator<T> {
    final Iterator<T> iter;
    int n;
    public Limiter(Iterator<T> iter, int n) {
        this.iter = iter;
        this.n = n;
    }
    public boolean hasNext() {
        return n > 0 ? iter.hasNext() : false;
    }
    public T next() {
        if(n-- < 0) throw new IllegalStateException();
        return iter.next();
    }
}