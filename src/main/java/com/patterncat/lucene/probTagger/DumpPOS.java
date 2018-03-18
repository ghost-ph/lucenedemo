package com.patterncat.lucene.probTagger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DumpPOS {

    /**
     * @param args
     * @throws IOException
     * @throws NumberFormatException
     */
    public static void main(String[] args) throws NumberFormatException, IOException {
        String fileName = "D:/lg/work/tx/dic/lexical.ctx.txt";
        FileReader filereader = new FileReader(fileName);
        BufferedReader br = new BufferedReader(filereader);

        //	read the table length
        int tableLen = Integer.parseInt(br.readLine());

        int nTotalFreq=0;
		/*for(int i=0 ;i<tableLen;i++)
		{
			line = fp.readLine();
			StringTokenizer st = new StringTokenizer(line,":" );
			int key=Integer.parseInt(st.nextToken());
			int val=Integer.parseInt(st.nextToken());//the every POS frequency

			symbolTable.put(key, val);
			nTotalFreq += val;
			//System.out.println("POS frequency:"+symbolTable.vals[i]);
			symbolList.add(key);
		}

		int[][] aContextArray=new int[tableLen][tableLen];
        for(int i=0;i<tableLen;i++)
		{
        	line = fp.readLine();
        	StringTokenizer st = new StringTokenizer(line," " );
		    for(int j=0 ;j<tableLen;j++)
		    {
		    	aContextArray[i][j]=Integer.parseInt(st.nextToken());
		    }
		}*/
    }

}
