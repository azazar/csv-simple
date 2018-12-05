/*
 * PROPRIETARY/CONFIDENTIAL
 */
package net.uo1.csv;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 *
 * @author Mikhail Yevchenko <123@ǟẓåẓạŗ.ćọ₥>
 */
class NullTerminatedSpliterator<T> implements Spliterator<T> {
    
    public Supplier<T> supplier;

    public NullTerminatedSpliterator(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        T value = supplier.get();
        
        if (value == null) {
            return false;
        }
        
        action.accept(value);
        
        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE;
    }
    
}
