package util.stream.spliterators;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class MergeSpliterator<T, U, R, K> extends Spliterators.AbstractSpliterator<R> {
    private final Spliterator<T> tSpliterator;
    private final Spliterator<U> uSpliterator;
    private final BiFunction<T, U, R> merger;
    private final Function<T, K> tkFunction;
    private final Function<U, K> ukFunction;

    private boolean done;
    private Spliterator<T> mapSpliteratorFinal;

    private Map<K, T> mapT;
    private Set<K> setK = new HashSet<>(); // Represents the consumed keys

    public MergeSpliterator(Spliterator<T> tSpliterator, Spliterator<U> uSpliterator, BiFunction<T, U, R> merger,
                            Function<T, K> tkFunction, Function<U, K> ukFunction) {
        super(tSpliterator.estimateSize() + uSpliterator.estimateSize(),
                (tSpliterator.characteristics() | uSpliterator.characteristics()) & ~Spliterator.SIZED);
        this.tSpliterator = tSpliterator;
        this.uSpliterator = uSpliterator;
        this.merger = merger;
        this.tkFunction = tkFunction;
        this.ukFunction = ukFunction;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        if(mapT == null) {
            mapT = new HashMap<>();
            while(tSpliterator.tryAdvance(t -> {
                if(!mapT.containsKey(tkFunction.apply(t))) mapT.put(tkFunction.apply(t), t);
            })) ;
        }
        // For each U merges with the corresponding T if there's any otherwise just converts the U
        if(uSpliterator.tryAdvance(u -> {
            action.accept(merger.apply(mapT.get(ukFunction.apply(u)), u));
            setK.add(ukFunction.apply(u)); // Adds U key into the setK to mark it as Consumed
        })) return true;
        else if(!done) { // Else for each remaining T converts it if there's not a corresponding U in the mapU
            mapSpliteratorFinal = mapT.values().stream().spliterator();
            done = true;
        }
        return mapSpliteratorFinal.tryAdvance(t -> {
            if(!setK.contains(tkFunction.apply(t))) action.accept(merger.apply(t, null));
        });
    }
}
