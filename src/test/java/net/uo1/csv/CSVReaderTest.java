/*
 * PROPRIETARY / CONFIDENTIAL
 */
package net.uo1.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSVReaderTest {
    
    @Test
    public void simpleTest() throws IOException {
        String test = "\"1\",2,3\n";

        try (CSVReader r = new CSVReader(new StringReader(test))) {
            String[] line;

            line = r.readLine();

            assertCSVParsedEqualsSource("simpleTest", new String[] { "1", "2", "3" }, line);

            assertNull(r.readLine());
        }
    }

    @Test
    public void enclosureTest() throws IOException {
        String test = "\"a\"\"sd\"\n";

        try (CSVReader r = new CSVReader(new StringReader(test))) {
            String[] line;

            line = r.readLine();

            assertEquals("a\"sd", line[0]);
        }
    }

    @Test
    public void enclosureTest2() throws IOException {
        String test = "asd\"zz\"asd\n";

        try (CSVReader r = new CSVReader(new StringReader(test))) {
            String[] line;

            line = r.readLine();

            assertEquals("asd\"zz\"asd", line[0]);
        }
    }

    @Test
    public void fullTest() throws IOException {
        String[][] test = new String[100][3];

        Random rand = new Random(0);

        char[] testChars = new char[] { CSV.DEFAULT.delimiter, CSV.DEFAULT.delimiter, 'a', 'b', 'c', 'd', 'e', 'f' };

        StringBuilder csv = new StringBuilder();

        for(int i = 0; i < test.length; i++) {
            String[] line = test[i];

            for(int j = 0; j < line.length; j++) {
                char[] str = new char[rand.nextInt(20)];

                for(int k = 0; k < str.length; k++) {
                    str[k] = testChars[rand.nextInt(testChars.length)];
                }

                line[j] = new String(str);
            }

            csv.append(CSV.toCSV(line));
        }

        int i = 0;

        try (CSVReader r = new CSVReader(new StringReader(csv.toString()))) {
            String[] read;
            while((read = r.readLine()) != null) {
                String[] expected = test[i++];

                assertCSVParsedEqualsSource("#" + i, expected, read);
            }

            assertEquals(test.length, i);
        }
    }

    private void assertCSVParsedEqualsSource(String message, String[] expected, String[] actual) {
        if (!Objects.deepEquals(expected, actual)) {
            StringBuilder details = new StringBuilder();
            for (int j = 0; j < Math.max(actual.length, expected.length); j++) {
                boolean match = j < Math.min(actual.length, expected.length) && actual[j].equals(expected[j]);

                details.append("#" + j + (match ? "+" : "-") + " R:\t" + (j < actual.length ? actual[j] : "-") + "\tX:\t" + (j < expected.length ? expected[j] : "-") + "\n");

                if (!match) break;
            }

            details.append(message + ":\n" + CSV.toCSV(expected) + "\n");

            throw new AssertionError(message + " x:" + String.join(",", expected) + " r:" + String.join(",", actual) + "\n" + details);
        }
    }

}
