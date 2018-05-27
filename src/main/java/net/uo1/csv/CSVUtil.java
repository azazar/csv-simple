/*
 * PROPRIETARY/CONFIDENTIAL
 */
package net.uo1.csv;

/**
 *
 * @author Mikhail Yevchenko <123@ǟẓåẓạŗ.ćọ₥>
 */
public class CSVUtil {
    
    public static String toCSV(String[] values) {
        return toCSV(values, CSV.DEFAULT_DELIMITER, CSV.DEFAULT_ENCLOSURE, CSV.DEFAULT_ESCAPE);
    }

    public static String toCSV(String[] values, char delimiter) {
        return toCSV(values, delimiter, CSV.DEFAULT_ENCLOSURE, CSV.DEFAULT_ESCAPE);
    }

    public static String toCSV(String[] values, char delimiter, char enclosure) {
        return toCSV(values, delimiter, enclosure, CSV.DEFAULT_ESCAPE);
    }
    
    private static class CSVSettings {
        char delimiter;
        char enclosure;
        char escape;
        
        String escapeString;
        String enclosureString;
        String escapedEscape;
        String escapedEnclosure;

        public CSVSettings(char delimiter, char enclosure, char escape) {
            this.delimiter = delimiter;
            this.enclosure = enclosure;
            this.escape = escape;
            
            escapeString = String.valueOf(escape);
            enclosureString = String.valueOf(enclosure);
            escapedEscape = String.valueOf(new char[] { escape, escape });
            escapedEnclosure = String.valueOf(new char[] { escape, enclosure });
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + this.delimiter;
            hash = 11 * hash + this.enclosure;
            hash = 11 * hash + this.escape;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CSVSettings other = (CSVSettings) obj;
            if (this.delimiter != other.delimiter) {
                return false;
            }
            if (this.enclosure != other.enclosure) {
                return false;
            }
            if (this.escape != other.escape) {
                return false;
            }
            return true;
        }
        

    }
    
    public static boolean shouldEnclose(String value, char delimiter, char enclosure, char escape) {
        if (value.isEmpty())
            return false;
        
        char char_ = value.charAt(0);
        
        if (char_ == delimiter || char_ == enclosure || char_ == escape) {
            return true;
        }
        
        for (int ofs = 1; ofs < value.length(); ofs++) {
            char_ = value.charAt(ofs);

            if (char_ == delimiter || char_ == escape) {
                return true;
            }
        }
        
        return false;
    }
    
    private static String escapeIfNecessary(String value, CSVSettings settings) {
        if (settings.escape == 0) {
            return value;
        }

        value = value.replace(settings.escapeString, settings.escapedEscape);
        value = value.replace(settings.enclosureString, settings.escapedEnclosure);
        
        return value;
    }
    
    private static CSVSettings lastSettings = null;
    
    public static String toCSV(String[] values, char delimiter, char enclosure, char escape) {
        int expectedLength = 0;
        for (String value : values) {
            expectedLength += value.length() + 1;
        }
        StringBuilder b = new StringBuilder(expectedLength);
        
        CSVSettings settings = lastSettings;
        
        if (settings == null || settings.delimiter != delimiter || settings.enclosure != enclosure || settings.escape != escape) {
            lastSettings = settings = new CSVSettings(delimiter, enclosure, escape);
        }
        
        for (String value : values) {
            if (b.length() > 0) {
                b.append(delimiter);
            }
            
            if (shouldEnclose(value, delimiter, enclosure, escape)) {
                b.append(enclosure).append(escapeIfNecessary(value, settings)).append(enclosure);
            }
            else {
                b.append(escapeIfNecessary(value, settings));
            }
        }
        
        b.append('\n');
        
        return b.toString();
    }
    
}
