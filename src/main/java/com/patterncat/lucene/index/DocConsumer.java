package com.patterncat.lucene.index;

import java.util.*;

/**
 * Represents the statistics collected on a Document. This primarily includes a
 * frequency list.
 */
public class DocConsumer {
    /** The file this document came from. Used in retrieval. */
    String filename;

    /** Included for redundancy. */
    public int docid;

    /** A frequency list mapping stemmed words (String) to frequency (int[1]) */
    HashMap<String, int[]> frequencyList;

    /** The document length, in words */
    int words;

    public DocConsumer() {
        frequencyList = new HashMap<String, int[]>();
        words = 0;
        filename = "";
        docid = 0;
    }

    /** Create a new instance and specify the filename/location */
    public DocConsumer(String f) {
        frequencyList = new HashMap<String, int[]>();
        words = 0;
        filename = f;
        docid = 0;
    }

    /**
     * Register this word with the frequency list.
     *
     * It either a) Defines a new occurrance in the frequency list b) Increments
     * the frequency
     */
    public void register(String word) {
        int[] val = (int[]) frequencyList.get(word);
        if (val == null) {
            val = new int[1];
            val[0] = 1;
            frequencyList.put(word, val);
        } else {
            val[0]++;
        }
        words++;
    }

    /** get the frequency of a term in this document */
    public int frequencyOf(String word) {
        int[] val = (int[]) frequencyList.get(word);
        if (val == null)
            return -1;

        return val[0];
    }

    /** get the probability of finding a term in this document */
    public double probabilityOf(String word) {
        int[] val = (int[]) frequencyList.get(word);
        if (val == null)
            return 0;

        return val[0] / (double) words;
    }

    /** get the number of unique stems in this document */
    public int uniqueWords() {
        return frequencyList.size();
    }

    /** The number of words in this document (including stop words). */
    public long words() {
        return words;
    }

    /**
     * @return The number of words that occur only once
     */
    public int singleOccurranceWords() {
        Iterator<String> i = frequencyList.keySet().iterator();

        int num = 0;

        while (i.hasNext()) {
            if (frequencyOf(i.next()) == 1)
                num++;
        }
        return num;
    }

    /**
     * Get a Hashtable of the most frequent n words. Don't use this for large
     * numbers, as it must search the entire Hashtable for each time.
     *
     * @param n
     *            How many results to return
     */
    public HashMap mostOccurring(int n) {
        // clean up after stopwords
        minimalClean();

        HashMap newFL = new HashMap(); // destination
        int max = 0, // current maximum frequency
                t = 0; // current frequency
        int[] maxA; // to store things in the new Hashtable
        Iterator it; // to loop through this list
        String c, // current word
                curMax = ""; // current maximum word

        for (int i = 0; i < n; i++) {
            max = 0;
            it = frequencyList.keySet().iterator();

            while (it.hasNext()) {
                c = (String) it.next();
                t = frequencyOf(c);
                if (max <= t && newFL.get(c) == null) {
                    curMax = c;
                    max = t;
                }
            }
            maxA = new int[1];

            // somehow, curMax was getting set, but
            // max was still 0 sometimes
            maxA[0] = frequencyOf(curMax);

            newFL.put(curMax, maxA);
        }

        return newFL;
    }

    /**
     * Retrieve all of the words with a probability greater than cutoff. It is
     * much faster than specifying an integer for how many most frequent words
     * you want, because it only makes a single pass.
     */
    public HashMap mostOccurring(double cutoff) {
		/*
		 * New algorithm: Compute the FREQUENCY cutoff using the cutoff words
		 * Gives us only integer operations
		 */
        // clean up after stopwords
        minimalClean();

        HashMap newFL = new HashMap(); // destination
        int t = 0; // current frequency
        int[] maxA; // to store things in the new Hashtable
        Iterator it; // to loop through this list
        String c; // current word
        int fCutoff = (int) (cutoff * words);

        it = frequencyList.keySet().iterator();

        while (it.hasNext()) {
            c = (String) it.next();
            t = frequencyOf(c);
            if (t > fCutoff) {
                maxA = new int[1];
                maxA[0] = t;
                newFL.put(c, maxA);
            }
        }

        if (newFL.size() == 0) {
            System.out.println("Nothing above the cutoff " + cutoff
                    + " in document " + docid);
        }

        return newFL;
    }

    /**
     * @return The highest frequency in this document
     */
    public int max_tf() {
        int t = 0, // current frequency
                max = 0;
        Iterator it; // to loop through this list
        String c; // current word

        it = frequencyList.keySet().iterator();

        while (it.hasNext()) {
            c = (String) it.next();
            t = frequencyOf(c);
            if (t > max)
                max = t;
        }

        return max;
    }

    /** Cleans up after stop words. */
    public void minimalClean() {
        frequencyList.remove("");
    }

    /** Cleans up the list to facilitate faster processing. */
    public void cleanList() {
        // step 1: remove the mess that stopwords leave behind
        minimalClean();

        // step 2: remove all single-occurrance words
        // this is the first step because it shrinks the list the most
        Iterator i = frequencyList.keySet().iterator();

        while (i.hasNext()) {
            if (frequencyOf((String) i.next()) == 1)
                frequencyList.remove(i);
        }
    }

    /**
     * Runs much faster than mostOccurring(int), but removes all single
     * occurrance words.
     */
    public HashMap destructiveMostOccurring(int m) {
		/*
		 * shrink the list in half in an O(n) stage This optimizes the O(mn)
		 * stage, where m is the parameter here.
		 */
        cleanList();
        return mostOccurring(m);
    }

    /**
     * Basically the same thing as destructiveMostOccurring(int) , except it has
     * a cutoff. The speed improvement won't be nearly as noticeable, as only
     * one pass is made on the frequencyList.
     *
     */
    public HashMap destructiveMostOccurring(double m) {
		/*
		 * shrink the list in half in an O(n) stage This optimizes the O(mn)
		 * stage, where m is the parameter here.
		 */
        cleanList();
        return mostOccurring(m);
    }
}
