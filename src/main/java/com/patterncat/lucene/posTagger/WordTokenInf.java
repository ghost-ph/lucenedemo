package com.patterncat.lucene.posTagger;

public class WordTokenInf {
    public String termText;
    public WordTypes data;
    public int start;
    public int end;
    public long cost = Constant.SINGLE_WORD_SMOOTH_VALUE;
    public long cde = 0;

    public WordTokenInf(int vertexFrom, int vertexTo, String word, WordTypes d) {
        start = vertexFrom;
        end = vertexTo;
        termText = word;
        data = d;
        if (d != null) {
            cost = d.total;
        }
    }

    public WordTokenInf(int vertexFrom, int vertexTo, String word, WordTypes d, long c) {
        start = vertexFrom;
        end = vertexTo;
        termText = word;
        data = d;
        cde = c;
        if (d != null) {
            cost = d.total;
        }
    }

    public String toString() {
        return "text:" + termText + " start:" + start + " end:" + end + "--------data:" + data;
    }
}
