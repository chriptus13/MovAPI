package util;

import util.iterator.*;

import java.util.*;
import java.util.function.*;

@FunctionalInterface
public interface QueriesNotStatic<T> extends Iterable<T> {
    static <T> QueriesNotStatic<T> empty() {
        return EmptyIterator::new;
    }

    static <T> QueriesNotStatic<T> of(Iterable<T> iter) {
        return iter::iterator;
    }

    static <T> QueriesNotStatic<T> of(T... values) {
        return () -> Arrays.asList(values).iterator();
    }

    static <T> QueriesNotStatic<T> of(Supplier<T[]> values) {
        return () -> Arrays.asList(values.get()).iterator();
    }

    static <T> QueriesNotStatic<T> generate(Supplier<T> src) {
        return () -> new Generator<>(src);
    }

    static <T> QueriesNotStatic<T> iterate(T seed, UnaryOperator<T> op) {
        return () -> new Iterate<>(seed, op);
    }

    default QueriesNotStatic<T> filter(Predicate<T> p) {
        return () -> new FilterIterator<>(this, p);
    }

    default <R> QueriesNotStatic<R> map(Function<T, R> mapper) {
        return () -> new MapIterator<>(this, mapper);
    }

    default <R> R reduce(R seed, BiFunction<R, T, R> acc) {
        for(T item : this) seed = acc.apply(seed, item);
        return seed;
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        for(T item : this) action.accept(item);
    }

    default int count() {
        int n = 0;
        for(T ignored : this) n++;
        return n;
    }

    default QueriesNotStatic<T> limit(int maxSize) {
        return () -> new Limiter<>(this.iterator(), maxSize);
    }

    default QueriesNotStatic<T> skip(int n) {
        return () -> {
            Iterator<T> iter = this.iterator();
            int count = n;
            while(iter.hasNext() && count-- > 0) iter.next();
            return iter;
        };
    }

    default QueriesNotStatic<T> takeWhile(Predicate<T> p) {
        return () -> new Iterator<T>() {
            Iterator<T> it = QueriesNotStatic.this.iterator();
            T curr;

            @Override
            public boolean hasNext() {
                if(curr != null) return p.test(curr);
                if(!it.hasNext()) return false;
                curr = it.next();
                return p.test(curr);
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException();
                T aux = curr;
                curr = null;
                return aux;
            }
        };
    }

    default <R> QueriesNotStatic<R> flatMap(Function<T, Iterable<R>> mapper) {
        return () -> new Iterator<>() {
            MapIterator<T, Iterator<R>> mapIterator = new MapIterator<>(QueriesNotStatic.this, t -> mapper.apply(t).iterator());
            Iterator<R> curr;

            @Override
            public boolean hasNext() {
                if(curr == null || !curr.hasNext())
                    if(mapIterator.hasNext()) curr = mapIterator.next();
                    else return false;
                return true;
            }

            @Override
            public R next() {
                if(!hasNext()) throw new NoSuchElementException();
                return curr.next();
            }
        };
    }

    default Object[] toArray() {
        List<T> res = new ArrayList<>();
        for(T item : this) res.add(item);
        return res.toArray();
    }
}
