package com.patterncat.lucene.rangeSearch;

import java.util.GregorianCalendar;

public class TestDateSearch {

    /**
     * @param args
     */
    public static void main(String[] args) {
        java.util.Calendar now = GregorianCalendar.getInstance();
        now.add(java.util.Calendar.YEAR, +100);
        System.out.println(now.getTime());
    }

}
