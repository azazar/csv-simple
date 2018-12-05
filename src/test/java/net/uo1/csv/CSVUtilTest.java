/*
 * PROPRIETARY / CONFIDENTIAL
 */
package net.uo1.csv;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSVUtilTest {

    public static void main(String[] args) {
        System.out.println(CSV.toCSV(new String[] { "asd", "aa,sd", "as\"d" }));;
    }
    
    
}
