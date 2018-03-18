package com.patterncat.lucene.basic;

import java.io.FileWriter;
import java.io.IOException;

public class TestMethod {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        writeFile("d:/test.txt","hellow world");
    }

    public static void writeFile(String fileName, String content)
            throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(content);
        writer.close();
    }
}
