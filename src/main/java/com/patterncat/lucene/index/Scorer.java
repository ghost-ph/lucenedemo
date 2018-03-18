package com.patterncat.lucene.index;

/**
 * A collection of weighting functions.
 *
 * All methods are static because there is no need for an object.
 */
public class Scorer {
    /**
     * A variation on Okapi term weighting, as mentioned in Project 3 of the
     * CMSC380 course offered by Dr. Martinovic.
     * @param tf Term frequency - the number of times a term occurs in a document
     * @param maxTf The maximum frequency of the most frequent term
     * @param docFreq Document frequency - the number of documents this term
     * occurs in
     * @param docLen The length, in words, of the document.
     * @param avgDocLen The average length, in words, of all documents.
     * @param numdocs The number of documents in the collection
     * @return A weight stored as a double.
     */
    public static double weight(int tf, int maxTf, int docFreq, int docLen,
                                int avgDocLen, int numdocs) {
        double temp = 0.4;
        temp += .6 * (tf / (tf + .5 + 1.5 * Math.log(docLen / avgDocLen)))
                * Math.log(numdocs / docFreq) / Math.log(numdocs);

        return temp;
    }
}
