package util.iterator;

import java.util.Iterator;
import java.util.function.UnaryOperator;

public class Iterate<T> implements Iterator<T> {
    T seed;
    final UnaryOperator<T> op;

    public Iterate(T seed, UnaryOperator<T> op) {
        this.seed = seed;
        this.op = op;
    }
    public boolean hasNext() {return true;}
    public T next() {return seed = op.apply(seed);}
}
