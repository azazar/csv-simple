/*
 * PROPRIETARY/CONFIDENTIAL
 */
package net.uo1.csv;

/**
 *
 * @author Mikhail Yevchenko
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
