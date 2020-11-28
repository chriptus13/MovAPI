package util.iterator;

import util.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static util.Box.empty;
import static util.Box.of;

public class FilterIterator<T> implements Iterator<T> {
    final Predicate<T> p;
    final Iterator<T> src;
    Box<T> curr;

    public FilterIterator(Iterable<T> src, Predicate<T> p) {
        this.src = src.iterator();
        this.p = p;
        curr = empty();
    }

    public boolean hasNext() {
        if(curr.isPresent()) return true;
        while(src.hasNext()) {
            T item = src.next();
            if(p.test(item)) {
                curr = of(item);
                return true;
            }
        }
        return false;
    }

    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        T aux = curr.getItem();
        curr = empty();
        return aux;
    }
}