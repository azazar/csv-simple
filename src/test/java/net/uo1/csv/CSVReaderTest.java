/*
 * PROPRIETARY / CONFIDENTIAL
 */
package net.uo1.csv;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSVReaderTest {
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
