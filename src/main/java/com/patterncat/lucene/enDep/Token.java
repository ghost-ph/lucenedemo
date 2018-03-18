package com.patterncat.lucene.enDep;

public class Token {
    public String termText;
    public int start;
    public int end;

    public Token(String word,int s, int e) {
        termText = word;
        start = s;
        end = e;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("text:" + termText + " start:" + start + " end:" + end );

        return buffer.toString();
    }
}
