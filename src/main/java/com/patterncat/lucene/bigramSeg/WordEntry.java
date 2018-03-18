package com.patterncat.lucene.bigramSeg;

public class WordEntry {
    public String word;
    public int freq;

    public WordEntry(String w,int f)
    {
        word = w;
        freq = f;
    }
    public String toString()
    {
        return word+":"+freq;
    }
}
