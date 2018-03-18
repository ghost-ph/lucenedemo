package com.patterncat.lucene.synonym;

import org.apache.lucene.search.similarities.DefaultSimilarity;

public class MySimilarity extends DefaultSimilarity {
    @Override
    public float coord(int overlap, int maxOverlap) {
        System.out.println("overlap "+overlap+" maxOverlap "+maxOverlap);
        return overlap / (float)maxOverlap;
    }

}

