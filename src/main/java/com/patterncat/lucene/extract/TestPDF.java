package com.patterncat.lucene.extract;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class TestPDF {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String fileName = //"D:/lg/work/hblocaltax/DB182628d01.pdf";
                "D:/lg/work/hblocaltax/1DE9A100d01.pdf";
        //"D:/lg/work/hblocaltax/plugin-China.pdf";
        PDDocument doc = PDDocument.load(fileName);
        PDFTextStripper stripper = new PDFTextStripper();
        String content = stripper.getText(doc);
        System.out.println(content);
    }

}
