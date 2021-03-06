/*
 * Copyright (c) 2018 Mikhail Yevchenko
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.uo1.csv;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
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
