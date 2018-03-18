package com.patterncat.lucene.basic;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;

public class WordCounter {
    public static void main ( String[] args ) {
        HashMap<String, Integer> wordcount = new HashMap<String, Integer>();

        String inputStr = "this is a apple";
        StringTokenizer st = new StringTokenizer(inputStr);
        while (st.hasMoreTokens()) {
            String word = st.nextToken();

            // Check if word is in HashMap
            if (wordcount.containsKey(word)) {
                // get number of occurrences for this word
                // increment it
                // and put back again
                wordcount.put(word, wordcount.get(word) + 1);
            } else {
                // this is first time we see this word, set value '1'
                wordcount.put(word, 1);
            }
        }
        for( Entry<String, Integer> e :wordcount.entrySet())
        {
            System.out.println(e.getKey()+" : "+ e.getValue());
        }
    }
}
