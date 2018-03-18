package com.patterncat.lucene.spell;

public class TestJaro {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String s1 ="sony";
        String s2 ="sonf";
        Jaro j = new Jaro();
        System.out.println(j.getSimilarity(s1, s2));
    }

}