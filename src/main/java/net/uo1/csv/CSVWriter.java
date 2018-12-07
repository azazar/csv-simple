/*
 * Copyright (c) 2018 Mikhail Yevchenko
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.uo1.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Mikhail Yevchenko <spam@uo1.net>
 */
public class CSVWriter implements Closeable {

    private Writer writer;
    private CSV settings;

    public CSVWriter(Writer writer) {
        this.writer = writer;
        settings = CSV.DEFAULT;
    }

    public CSVWriter(Writer writer, CSV settings) {
        this.writer = writer;
        this.settings = settings;
    }

    /**
     * Closes underlying writer
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        writer.close();
    }
}
