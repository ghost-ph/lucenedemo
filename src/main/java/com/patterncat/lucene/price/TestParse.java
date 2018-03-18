package com.patterncat.lucene.price;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class TestParse {

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        Number number = NumberFormat.getCurrencyInstance(Locale.US).parse("$123.45");
        System.out.println(number);
    }

}
