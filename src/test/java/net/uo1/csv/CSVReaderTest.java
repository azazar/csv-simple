/*
 * PROPRIETARY / CONFIDENTIAL
 */
package net.uo1.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
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
    
    public static void main(String[] args) throws Exception {
        String test = "14\r,\r,http://thumbs.pornoplum.com/contents/videos_screenshots/0/14/352x198/1.jpg,MILF nice-looking to solicit,\"MILF, Big Boobs, Brunettes, MILFs, Blowjobs, Mom, Russian\",\"Big Tits,MILF,Mature,Russian,Mom,big boobs,straight\",\"<iframe width=\\\"640\\\" height=\\\"480\\\" src=\\\"http://www.pornoplum.com/embed/14\\\" frameborder=\\\"0\\\" allowfullscreen webkitallowfullscreen mozallowfullscreen oallowfullscreen msallowfullscreen></iframe>\",,,milf-nice-looking-to-solicit\r\n";
        
        try (CSVReader r = new CSVReader(new StringReader(test))) {
            r.setStripCrBeforeLf(true);

            String[] line = r.readLine();
            
            if (line[0].endsWith("\r")) {
                System.err.println("not stripped in first cell");
            }
            if (line[line.length-1].endsWith("\r")) {
                System.err.println("not stripped after last cell");
            }
            
            for (String e : line) {
                System.out.println(e);
            }
            System.out.println();
        }
    }
}
