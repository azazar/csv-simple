package net.uo1.csv.example;

import net.uo1.csv.CSV;
import net.uo1.csv.CSVReader;
import net.uo1.csv.CSVWriter;

import java.io.*;
import java.util.Map;

public class Example {

    private static PrintStream out = new PrintStream(System.out) {
        @Override
        public void close() {}
    };

    public static void readExample1() throws IOException {
        // the file with header "field1,field2"
        try (CSVReader r = CSV.RFC4180.createReader(new FileReader("numbers.csv"))) {
            Map<String, String> row;

            while ((row = r.readRow()) != null) {
                System.out.println(row.get("field1") + " : " + row.get("field2"));
            }
        }
    }

    public static void readExample2() throws IOException {
        // the file with header "field1,field2"
        try (CSVReader r = CSV.RFC4180.createReader(new FileReader("numbers.csv"))) {
            r.rowStream(true).forEach(
                row -> System.out.println(row.get("field1") + " : " + row.get("field2"))
            );
        }
    }

    public static void writeExample1() throws IOException {
        try (CSVWriter w = new CSVWriter(new PrintWriter(out))) {
            for (int i = 0; i < 10; i++) {
                w.write("example1", i, "row" + i);
            }
        }
    }

    public static void writeExample2() {
        for (int i = 0; i < 10; i++) {
            out.print(CSV.toCSV(new String[] { "example2", "" + i, "row" + i }));
        }
        out.flush();
    }

    public static void main(String[] args) throws Exception {
        readExample1();
        readExample2();
        writeExample1();
        writeExample2();
    }

}
