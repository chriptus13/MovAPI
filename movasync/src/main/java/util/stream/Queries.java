package util.stream;

import util.stream.spliterators.MergeSpliterator;

import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Queries {
    public static <T, U, R, K> Stream<R> merge(Spliterator<T> tSpliterator, Spliterator<U> uSpliterator, BiFunction<T, U, R> merger, Function<T, K> tkFunction, Function<U, K> ukFunction) {
        return StreamSupport.stream(new MergeSpliterator<>(tSpliterator, uSpliterator, merger, tkFunction, ukFunction), false);
    }
}
