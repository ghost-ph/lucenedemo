package com.patterncat.lucene.trainUnknow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ExtractPersonName {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        FileReader in = new FileReader("D:/lg/wordSeg/PFR/199801.txt");
        BufferedReader buffer = new BufferedReader(in);
        String line;
        int linecount=0;

        while ((line = buffer.readLine()) != null) {
            linecount++;
            if(line.equals(""))
                continue;
            StringTokenizer st = new StringTokenizer(line,"  ");
            while(st.hasMoreTokens())
            {
                String wordPOS = st.nextToken();
                if(wordPOS.endsWith("nr"))
                {
                    System.out.println(wordPOS);
                }
            }
        }
    }

}

