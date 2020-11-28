package util.stream;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Cache {
    public static <T> Supplier<Stream<T>> of(Supplier<Stream<T>> dataSrc) {
        final Recorder<T> rec = new Recorder<>(dataSrc);
        return () -> StreamSupport.stream(rec.cacheIterator(), false);
    }

    private static class Recorder<T> {
        final Supplier<Stream<T>> dataSrc;
        Spliterator<T> src;
        final List<T> cache = new ArrayList<>();
        long estimateSize;
        boolean hasNext = true;

        public Recorder(Supplier<Stream<T>> dataSrc) {
            this.dataSrc = dataSrc;
        }

        public synchronized boolean getOrAdvance(final int index, Consumer<? super T> cons) {
            if (index < cache.size()) {
                // If it is in cache then just get if from the corresponding index.
                cons.accept(cache.get(index));
                return true;
            } else if (hasNext)
                // If not in cache then advance the src iterator
                hasNext = src.tryAdvance(item -> {
                    cache.add(item);
                    cons.accept(item);
                });
            return hasNext;
        }

        public Spliterator<T> cacheIterator() {
            if(src==null) src = dataSrc.get().spliterator();
            return new Spliterators.AbstractSpliterator<>(estimateSize = src.estimateSize(), src.characteristics()) {
                int index = 0;

                public boolean tryAdvance(Consumer<? super T> cons) {
                    return getOrAdvance(index++, cons);
                }

                public Comparator<? super T> getComparator() {
                    return src.getComparator();
                }
            };
        }
    }
}
