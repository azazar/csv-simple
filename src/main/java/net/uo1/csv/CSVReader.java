/*
 * PROPRIETARY/CONFIDENTIAL
 */
package net.uo1.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author mih
 */
public class CSVReader implements Closeable {
    
    private Reader reader;
    
    private char delimiter;
    private char enclosure;
    private char escape;
    private char newLine = '\n';

    public CSVReader(Reader reader, char delimiter, char enclosure, char escape) {
        this.reader = reader;
        this.delimiter = delimiter;
        this.enclosure = enclosure;
        this.escape = escape;
    }

    public CSVReader(Reader reader) {
        this(reader, CSV.DEFAULT_DELIMITER, CSV.DEFAULT_ENCLOSURE, CSV.DEFAULT_ESCAPE);
    }
    
    private int expectedLength = 0;
    
    private boolean eof = false;
    
    public String[] readLine() throws IOException {
        if (eof) {
            return null;
        }
        
        StringArrayBuilder line = new StringArrayBuilder(expectedLength);
        
        int ch = -1, prev;
        StringBuilder cell = new StringBuilder();
        boolean enclosed = false;
        
        for(;;) {
            prev = ch;
            ch = reader.read();
            
            if (ch == -1) {
                eof = true;
                if (cell.length() > 0) {
                    line.add(cell.toString());;
                }

                if (!line.isEmpty()) {
                    return line.build();
                }

                return null;
            }
            
            if (enclosed) {
                if (ch == enclosure) {
                    if (prev == enclosure) {
                        cell.append(enclosure);
                    }
                    enclosed = false;
                }
                else {
                    cell.append((char)ch);
                }
            }
            else {
                if (ch == enclosure) {
                    enclosed = true;
                }
                else if (ch == delimiter || ch == newLine) {
                    line.add(cell.toString());
                    cell.setLength(0);

                    if (ch == newLine) {
                        return line.build();
                    }
                }
                else {
                    cell.append((char)ch);
                }
            }
            
        }
    }
    
    public void ensureBuffered() {
        if (!(reader instanceof BufferedReader)) {
            reader = new BufferedReader(reader);
        }
    }
    
    private String[] header = null;
    
    public void readHeader() throws IOException {
        if (header != null) {
            throw new IllegalStateException();
        }
        
        header = readLine();
    }
    
    public void applyHeader(String[] header) {
        if (this.header != null) {
            throw new IllegalStateException();
        }
        
        this.header = header;
    }

    public String[] getHeader() {
        return this.header == null ? null : this.header.clone();
    }
    
    public Map<String, String> readRow() throws IOException {
        if (header == null) {
            readHeader();
            if (header == null) {
                return null;
            }
        }
        
        String[] line = readLine();
        
        if (line == null) {
            return null;
        }
        
        return new AbstractMap<String, String>() {
        
            @Override
            public Set<Map.Entry<String, String>> entrySet() {
                return new AbstractSet<Map.Entry<String, String>>() {

                    @Override
                    public Iterator<Map.Entry<String, String>> iterator() {
                        return new Iterator<Entry<String, String>>() {
                            
                            int index = -1;
                            
                            @Override
                            public boolean hasNext() {
                                return (index + 1) < line.length;
                            }

                            @Override
                            public Map.Entry<String, String> next() {
                                index++;
                                
                                return new Entry<String, String>() {
                                    @Override
                                    public String getKey() {
                                        return header.length > index ? header[index] : "$" + Integer.toString(index + 1);
                                    }

                                    @Override
                                    public String getValue() {
                                        return line[index];
                                    }

                                    @Override
                                    public String setValue(String value) {
                                        throw new UnsupportedOperationException("Not supported.");
                                    }

                                };
                            }
                        };
                    }

                    @Override
                    public int size() {
                        return line.length;
                    }
                
                };
            }
        
        };
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }
    
    private String[] readLineNoIOException() {
        try {
            return readLine();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private Map<String, String> readRowNoIOException() {
        try {
            return readRow();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Stream<String[]> lineStream(boolean parallel) {
        return StreamSupport.stream(new NullTerminatedSpliterator<>(this::readLineNoIOException), parallel);
    }

    public Stream<Map<String, String>> rowStream(boolean parallel) {
        return StreamSupport.stream(new NullTerminatedSpliterator<>(this::readRowNoIOException), parallel);
    }

}
