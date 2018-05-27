/*
 * PROPRIETARY / CONFIDENTIAL
 */
package net.uo1.csv;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSVUtilTest {

    public static void main(String[] args) {
        System.out.println(CSVUtil.toCSV(new String[] { "asd", "aa,sd", "as\"d" }));;
    }
    
    
}
