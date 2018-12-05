package net.uo1.csv;

import java.io.Reader;

/**
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSV {

    public static final char RFC4180_DELIMITER = ',';
    public static final char RFC4180_ENCLOSURE = '"';
    public static final char RFC4180_ESCAPE = (char)0;

    public static final char DEFAULT_PHP_ESCAPE = '\\';

    /**
     *
     * RFC4180-compatible settings
     */
    public static final CSV RFC4180 = new CSV(RFC4180_DELIMITER, RFC4180_ENCLOSURE, RFC4180_ESCAPE);

    /**
     *
     * PHP-compatible settings
     */
    public static final CSV PHP = new CSV(RFC4180_DELIMITER, RFC4180_ENCLOSURE, DEFAULT_PHP_ESCAPE);

    /**
     *
     * Used to avoid garbage creation
     */
    private static CSV lastSettings = null;

    /**
     * Field delimiter character. Zero value disables it.
     *
     * <p>See also: <a href="https://www.ietf.org/rfc/rfc4180.txt">RFC #4180 2.7</a>
     */
    public final char delimiter;

    /**
     * Field enclosure character. Zero value disables it.
     *
     * <p>See also: <a href="https://www.ietf.org/rfc/rfc4180.txt">RFC #4180 2.7</a>
     */
    public final char enclosure;

    /**
     * Escape character. Zero value disables it.
     *
     * <p>Note: Usually an enclosure character is escaped inside a field by doubling it; however,
     * the escape character can be used as an alternative. Zero value disables it.</p>
     *
     * <p>See also: <a href="http://php.net/manual/ru/function.fgetcsv.php">PHP Manual fgetcsv</a>
     */
    public final char escape;

    /**
     * Creates new instance of CSV class
     *
     * @param delimiter Field delimiter character. Zero value disables it.
     * @param enclosure Field enclosure character. Zero value disables it.
     * @param escape Escape character. Zero value disables it.
     */
    public CSV(char delimiter, char enclosure, char escape) {
        this.delimiter = delimiter;
        this.enclosure = enclosure;
        this.escape = escape;
    }

    /**
     * Encodes single line of CSV file
     *
     * @param values array that contains column values
     * @return CSV encoded line
     */
    public String encode(String[] values) {
        return toCSV(values, delimiter, enclosure, escape);
    }

    /**
     * Creates new CSVReader
     *
     * @param reader that reads raw CSV input
     * @return new CSVReader instance
     */
    public CSVReader createReader(Reader reader) {
        return new CSVReader(reader, this.delimiter, this.enclosure, this.escape);
    }

    /**
     * Creates new CSVReader with rfc4180 settings
     *
     * @param reader that reads raw CSV input
     * @return new CSVReader instance
     */
    public static CSVReader open(Reader reader) {
        return RFC4180.createReader(reader);
    }

    /**
     * Encodes single line of CSV file
     *
     * @param values array that contains column values
     * @return CSV encoded line
     */
    public static String toCSV(String[] values) {
        return toCSV(values, RFC4180_DELIMITER, RFC4180_ENCLOSURE, RFC4180_ESCAPE);
    }

    /**
     * Encodes single line of CSV file
     *
     * @param values array that contains column values
     * @param delimiter Field delimiter character. Zero value disables it.
     * @param enclosure Field enclosure character. Zero value disables it.
     * @return CSV encoded line
     */
    public static String toCSV(String[] values, char delimiter, char enclosure) {
        return toCSV(values, delimiter, enclosure, RFC4180_ESCAPE);
    }

    private static boolean shouldEnclose(String value, CSV settings) {
        if (value.isEmpty())
            return false;

        char char_ = value.charAt(0);

        if (char_ == settings.delimiter || char_ == settings.enclosure) {
            return true;
        }

        for (int ofs = 1; ofs < value.length(); ofs++) {
            char_ = value.charAt(ofs);

            if (char_ == settings.delimiter || char_ == settings.enclosure) {
                return true;
            }
        }

        return false;
    }

    private static void escapeEnclosures(String value, CSV settings, StringBuilder builder) {
        if (settings.enclosure == 0) {
            builder.append(value);
            return;
        }

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);

            if (ch == settings.escape) {
                builder.append(ch).append(ch);
            }
            else if (ch == settings.enclosure) {
                builder.append(ch).append(ch);
            }
            else {
                builder.append(ch);
            }
        }
    }

    /**
     * Encodes single line of CSV file
     *
     * @param values array that contains column values
     * @param delimiter Field delimiter character. Zero value disables it.
     * @param enclosure Field enclosure character. Zero value disables it.
     * @param escape Escape character. Zero value disables it.
     * @return CSV encoded line
     */
    public static String toCSV(String[] values, char delimiter, char enclosure, char escape) {
        int expectedLength = 0;
        for (String value : values) {
            expectedLength += value.length() + 1;
        }
        StringBuilder b = new StringBuilder(expectedLength);

        CSV settings = lastSettings;

        if (settings == null || settings.delimiter != delimiter || settings.enclosure != enclosure || settings.escape != escape) {
            lastSettings = settings = new CSV(delimiter, enclosure, escape);
        }

        boolean first = true;

        for (String value : values) {
            if (first) {
                first = false;
            }
            else {
                b.append(delimiter);
            }

            if (shouldEnclose(value, settings)) {
                b.append(enclosure);
                escapeEnclosures(value, settings, b);
                b.append(enclosure);
            }
            else {
                escapeEnclosures(value, settings, b);
            }
        }

        b.append('\n');

        return b.toString();
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
        final CSV other = (CSV) obj;
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
