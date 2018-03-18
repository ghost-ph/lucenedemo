package com.patterncat.lucene.trainHMM;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileReader;

public class CountTrans {

    private Integer length = PartOfSpeech.values().length;
    private String PATH = "199801.txt";
    public int[][] contextFreq = new int[length][length];
    public int[] posFreq = new int[length];
    public Map<String, int[]> wordPOSFreq = new HashMap<String, int[]>();

    /**
     * @throws IOException
     */
    public void readCorpus() throws IOException {
        //BufferedReader br = new BufferedReader(new FileReader(new File(PATH)));
        InputStream file = new FileInputStream(new File(PATH));

        BufferedReader br = new BufferedReader(new InputStreamReader(file,"GBK"));

        String line;
        while ((line = br.readLine()) != null) {
            if ("".equals(line)) {
                continue;
            }
            anaylsis(line);
        }
    }

    /**
     * 统锟狡达拷锟斤拷
     *
     * @param line
     */
    public void anaylsis(String line) {
        StringTokenizer st = new StringTokenizer(line, "  ");
        st.nextToken();

        int index = PartOfSpeech.start.ordinal();
        int nextIndex = 0;
        while (st.hasMoreTokens()) {
            StringTokenizer stk = new StringTokenizer(st.nextToken(), "/");
            String word = stk.nextToken();
            String next = stk.nextToken();
            try {
                nextIndex = PartOfSpeech.valueOf(next.toLowerCase()).ordinal();
            } catch (IllegalArgumentException ex) {
                nextIndex = PartOfSpeech.unknow.ordinal();
            }

            //System.out.println(word);
            int[] gs = wordPOSFreq.get(word);
            if (null != gs) {
                //System.out.println("nextIndex " +nextIndex +" "+(gs==null) );
                gs[nextIndex]++;
                //gs[0]++;
            } else {
                gs = new int[PartOfSpeech.values().length];
                gs[nextIndex] = 1;
            }
            wordPOSFreq.put(word, gs);
            contextFreq[index][nextIndex]++;
            posFreq[nextIndex]++;
            index = nextIndex;
        }
        nextIndex = PartOfSpeech.end.ordinal();
        contextFreq[index][nextIndex]++;
        posFreq[nextIndex]++;
    }
}

