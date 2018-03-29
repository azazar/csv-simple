/*
 * PROPRIETARY/CONFIDENTIAL
 */
package net.uo1.csv;

/**
 *
 * @author Mikhail Yevchenko <123@ǟẓåẓạŗ.ćọ₥>
 */
public class CSVUtil {
    
    public String toCSV(String[] values) {
        return toCSV(values, CSV.DEFAULT_DELIMITER, CSV.DEFAULT_ENCLOSURE, CSV.DEFAULT_ESCAPE);
    }

    public String toCSV(String[] values, char delimiter) {
        return toCSV(values, delimiter, CSV.DEFAULT_ENCLOSURE, CSV.DEFAULT_ESCAPE);
    }

    public String toCSV(String[] values, char delimiter, char enclosure) {
        return toCSV(values, delimiter, enclosure, CSV.DEFAULT_ESCAPE);
    }
    
    public boolean shouldEnclose(String value, char delimiter, char enclosure, char escape) {
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

    public String toCSV(String[] values, char delimiter, char enclosure, char escape) {
        int expectedLength = 0;
        for (String value : values) {
            expectedLength += value.length() + 1;
        }
        StringBuilder b = new StringBuilder(expectedLength);
        
        char[] escapedEnclosureCharArray = new char[] { enclosure, enclosure };
        String escapedEnclosure = new String(escapedEnclosureCharArray);
        String enclosureString = String.valueOf(enclosure);
        
        for (String value : values) {
            if (b.length() > 0) {
                b.append(delimiter);
            }
            
            if (shouldEnclose(value, delimiter, enclosure, escape)) {
                b.append(enclosure).append(value.replace(enclosureString, escapedEnclosure)).append(enclosure);
            }
            else {
                b.append(value);
            }
        }
        
        b.append('\n');
        
        return b.toString();
    }
    
}
