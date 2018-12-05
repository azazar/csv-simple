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
 * @author Mikhail Yevchenko
 */
public class CSVReader implements Closeable, Iterable<Map<String, String>> {
    
    private Reader reader;
    
    private char delimiter;
    private char enclosure;
    private char escape;
    private char newLine = '\n';
    
    private boolean stripCrBeforeLf = true;

    /**
     * Creates new CSVReader
     *
     * @param reader
     * @param delimiter Field delimiter character. Zero value disables it.
     * @param enclosure Field enclosure character. Zero value disables it.
     * @param escape Escape character. Zero value disables it.
     */
    public CSVReader(Reader reader, char delimiter, char enclosure, char escape) {
        this.reader = reader;
        this.delimiter = delimiter;
        this.enclosure = enclosure;
        this.escape = escape;
    }

    /**
     * Creates new CSVReader with rfc4180 settings
     *
     * @param reader
     */
    public CSVReader(Reader reader) {
        this(reader, CSV.RFC4180_DELIMITER, CSV.RFC4180_ENCLOSURE, CSV.RFC4180_ESCAPE);
    }

    /**
     *
     * @return support flag for CRLF line endings
     */
    public boolean isStripCrBeforeLf() {
        return stripCrBeforeLf;
    }

    /**
     * Set support flag for CRLF line endings (enabled to default)
     *
     * @param stripCrBeforeLf
     */
    public void setStripCrBeforeLf(boolean stripCrBeforeLf) {
        this.stripCrBeforeLf = stripCrBeforeLf;
    }
    
    private int expectedLength = 0;
    
    private boolean eof = false;

    /**
     * Reads single line from CSV file as string array
     *
     * @return decoded csv string values
     * @throws IOException
     */
    public String[] readLine() throws IOException {
        if (eof) {
            return null;
        }
        
        StringArrayBuilder line = new StringArrayBuilder(expectedLength);
        
        int ch;
        StringBuilder cell = new StringBuilder();
        boolean enclosed = false;
        
        for(;;) {
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
            
            if (ch == escape) {
                ch = reader.read();
                if (ch != -1) {
                    cell.append((char)ch);
                }
            }
            else if (enclosed) {
                if (ch == enclosure) {
                    ch = reader.read();

                    if (ch == -1) {
                        // eof
                    }
                    else if (ch == enclosure) {
                        cell.append(enclosure);
                    }
                    else if (ch == delimiter || ch == newLine) {
                        line.add(cell.toString());

                        if (ch == newLine)
                            return line.build();

                        cell.setLength(0);

                        enclosed = false;
                    }
                    else {
                        enclosed = false;
                    }
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
                    if (ch == newLine && cell.length() > 0 && stripCrBeforeLf && cell.charAt(cell.length() - 1) == '\r') {
                        cell.setLength(cell.length() - 1);
                    }

                    line.add(cell.toString());

                    if (ch == newLine) {
                        return line.build();
                    }

                    cell.setLength(0);
                }
                else if (ch == escape) {
                }
                else {
                    cell.append((char)ch);
                }
            }
            
        }
    }

    /**
     * Wraps internal reader with BufferedReader if it's not done already
     *
     * @see BufferedReader
     */
    public void ensureBuffered() {
        if (!(reader instanceof BufferedReader)) {
            reader = new BufferedReader(reader);
        }
    }
    
    private String[] header = null;

    /**
     * Reads CSV header line. readRow(...) does this automatically.
     *
     * @throws IOException
     */
    public void readHeader() throws IOException {
        if (header != null) {
            throw new IllegalStateException();
        }
        
        header = readLine();
    }

    /**
     * Sets field names for readRow(...)
     *
     * @param header field names to be used as keys in readRow(...)
     * @throws IOException
     */
    public void applyHeader(String[] header) {
        if (this.header != null) {
            throw new IllegalStateException();
        }
        
        this.header = header;
    }

    /**
     *
     * @return current field names
     */
    public String[] getHeader() {
        return this.header == null ? null : this.header.clone();
    }

    /**
     * Reads single line from CSV file as map with keys specified in file
     * header or using applyHeader(...)
     *
     * @return decoded csv string values
     * @throws IOException
     */
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

    /**
     * Closes underlying reader
     *
     * @throws IOException
     */
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

    @Override
    public Iterator<Map<String, String>> iterator() {
        return new Iterator<Map<String, String>>() {
            
            private Map<String, String> read() {
                try {
                    return readRow();
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            private Map<String, String> next = read();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Map<String, String> next() {
                if (next == null) {
                    throw new IllegalStateException();
                }
                Map<String, String> current = next;
                next = read();
                return current;
            }

        };
    }

}
