package util.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class MapIterator<T, R> implements Iterator<R> {
    final Function<T, R> mapper;
    final Iterator<T> src;

    public MapIterator(Iterable<T> src, Function<T, R> mapper) {
        this.src = src.iterator();
        this.mapper = mapper;
    }

    public boolean hasNext() {
        return src.hasNext();
    }

    public R next() {
        return mapper.apply(src.next());
    }
}
