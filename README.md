# csv-simple

csv-simple is a simple CSV reader/writer for Java without any dependencies.

## Usage

### Install csv-simple to local repository

    $ git clone https://github.com/azazar/csv-simple
    $ cd csv-simple
    $ mvn install
    
### Add maven dependency

    <dependencies>
        <dependency>
            <groupId>net.uo1</groupId>
            <artifactId>csv-simple</artifactId>
            <version>1.1.2</version>
        </dependency>
    </dependencies>


## Usage Examples

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
