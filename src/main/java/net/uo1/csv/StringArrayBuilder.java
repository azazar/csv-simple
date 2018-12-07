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

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
class StringArrayBuilder {
    
    private static final String[] EMPTY = new String[0];

    private String[] array = EMPTY;
    
    private int size = 0;

    public StringArrayBuilder(int capacity) {
        array = new String[capacity];
    }
    
    public void add(String s) {
        if (array.length == size) {
            String[] n = new String[(size + 1) * 2];
            System.arraycopy(array, 0, n, 0, size);
            array = n;
        }
        
        array[size++] = s;
    }
    
    public String[] build() {
        if (array.length != size) {
            String[] n = new String[size];
            System.arraycopy(array, 0, n, 0, size);
            return n;
        }

        return array;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    
}
