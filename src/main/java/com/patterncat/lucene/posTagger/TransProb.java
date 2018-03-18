package com.patterncat.lucene.posTagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TransProb {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String fileName = "D:/lg/work/SSeg/Data/2.1/dic/lexical.ctx.txt";
        int tableLen;
        BufferedReader fp = null;
        String line;

        InputStream file = new FileInputStream(new File(fileName));

        fp = new BufferedReader(new InputStreamReader(file, "GBK"));

        // read the table length
        tableLen = Integer.parseInt(fp.readLine());

        ArrayList<Integer> symbolList = new ArrayList<Integer>(50);
        // Read the context

        int nTotalFreq = 0;
        for (int i = 0; i < tableLen; i++) {
            line = fp.readLine();
            StringTokenizer st = new StringTokenizer(line, ":");
            int key = Integer.parseInt(st.nextToken());
            int val = Integer.parseInt(st.nextToken());// the every POS
            // frequency

            nTotalFreq += val;
            System.out.println("词性  " + getPOSName(key) + " 频率:" + val);
            symbolList.add(key);
        }

        int[][] aContextArray = new int[tableLen][tableLen];
        for (int i = 0; i < tableLen; i++) {
            line = fp.readLine();
            StringTokenizer st = new StringTokenizer(line, " ");
            for (int j = 0; j < tableLen; j++) {
                aContextArray[i][j] = Integer.parseInt(st.nextToken());
                System.out.println("上一个词性  " + getPOSName(symbolList.get(i))
                        +" 下一个词性 "+ getPOSName(symbolList.get(j))+ " 频率:" + aContextArray[i][j]);

            }
        }

        fp.close();

    }

    public static String getPOSName(int code)
    {
        if(code==1)
            return "BOS";
        if(code==4)
            return "EOS";
        if(code%256!=0)
        {
            char c[] = new char[2];
            c[0] = (char) (code>>8);
            c[1] = (char) (code%256);
            return new String(c);
        }
        return String.valueOf((char) (code>>8));
    }

}
