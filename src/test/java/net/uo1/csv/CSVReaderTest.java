/*
 * PROPRIETARY / CONFIDENTIAL
 */
package net.uo1.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSVReaderTest {
    
    //@Test
    public void fullTest() throws IOException {
        String[][] test = new String[100][3];
        
        Random rand = new Random(0);
        char[] testChars = new char[] { CSV.DEFAULT_DELIMITER, CSV.DEFAULT_ENCLOSURE, CSV.DEFAULT_ESCAPE, 'a', 'b', 'c', 'd', 'e', 'f' };
        
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
            
            csv.append(CSVUtil.toCSV(line));
        }
        
        int i = 0;

        try (CSVReader r = new CSVReader(new StringReader(csv.toString()))) {
            String[] read;
            while((read = r.readLine()) != null) {
                String[] expected = test[i++];

                assertArrayEquals("@" + i, expected, read);
            }
            
            assertEquals(test.length, i);
        }
    }

    @Test
    public void simpleTest() throws IOException {
        String test = "\"1\",2,3\n";
        
        try (CSVReader r = new CSVReader(new StringReader(test))) {
            String[] line;

            line = r.readLine();
            
            assertEquals(3, line.length);
            assertEquals("1", line[0]);
            assertEquals("2", line[1]);
            assertEquals("3", line[2]);
            
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
    
}
