package com.patterncat.lucene.nlp;

import java.io.BufferedReader;
import java.util.HashSet;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StopWordsSet {

    private static StopWordsSet worddic = null;
    private HashSet<String> dic;

    private StopWordsSet(String filename) {
        dic = new HashSet<String>();
        String dataline;
        try {
            InputStream setdata = getClass().getResourceAsStream(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    setdata, "GBK"));

            while ((dataline = in.readLine()) != null) {
                if ((dataline.length() == 0)) {
                    continue;
                }
                dic.add(dataline.intern());
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Exception loading data file" + filename + " "
                    + e);
            e.printStackTrace();
        }
    }

    public static StopWordsSet getInstance(String filename) {
        if (StopWordsSet.worddic == null) {
            StopWordsSet.worddic = new StopWordsSet(filename);
        }
        return StopWordsSet.worddic;
    }

    public boolean isStopWord(String str) {
        return dic.contains(str);
    }
}
