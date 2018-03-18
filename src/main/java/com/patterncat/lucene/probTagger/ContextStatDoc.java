package com.patterncat.lucene.probTagger;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * 转移概率
 */
public class ContextStatDoc {

    private static int[][] transProbs;

    private static ContextStatDoc cs = new ContextStatDoc();

    public static ContextStatDoc getInstance() {
        return cs;
    }

    private ContextStatDoc() {
        int matrixLength = PartOfSpeech.values().length;
        transProbs = new int[matrixLength][];

        for (int i = 0; i < PartOfSpeech.values().length; ++i) {
            transProbs[i] = new int[matrixLength];
        }
        load("lexical.ctx.txt");
		/*addTrob(PartOfSpeech.Start, PartOfSpeech.Street, 10000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.LandMark, 1000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.District, 100000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.Village, 1000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.Town, 100000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.City, 1000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.County, 1000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.Country, 1000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.Province, 1000000);
		addTrob(PartOfSpeech.Start, PartOfSpeech.Municipality, 1000000);

		addTrob(PartOfSpeech.Province, PartOfSpeech.City, 100000000);
		addTrob(PartOfSpeech.Province, PartOfSpeech.County, 1000000);
		addTrob(PartOfSpeech.Province, PartOfSpeech.Town, 10);
		addTrob(PartOfSpeech.Province, PartOfSpeech.End, 10000000);
		addTrob(PartOfSpeech.Province, PartOfSpeech.SuffixMunicipality, -10000000);
		addTrob(PartOfSpeech.Municipality, PartOfSpeech.District, 1000000);
		addTrob(PartOfSpeech.Municipality, PartOfSpeech.County, 10000000);
		addTrob(PartOfSpeech.Municipality, PartOfSpeech.Street, 10000);


		addTrob(PartOfSpeech.Municipality, PartOfSpeech.End, 10000);
		addTrob(PartOfSpeech.City, PartOfSpeech.County, 10000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.Town, 10000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.City, 1000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.Street, 100000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.District, 10000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.LandMark, 1000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.Country, 1000);
		addTrob(PartOfSpeech.City, PartOfSpeech.End, 1000);
		addTrob(PartOfSpeech.City, PartOfSpeech.SuffixCity, -1000000);
		addTrob(PartOfSpeech.City, PartOfSpeech.SuffixLandMark, -100000);
		addTrob(PartOfSpeech.City, PartOfSpeech.SuffixStreet, -100000);
		addTrob(PartOfSpeech.City, PartOfSpeech.SuffixCounty, -100000);
		addTrob(PartOfSpeech.County, PartOfSpeech.Village, 10000);
		addTrob(PartOfSpeech.County, PartOfSpeech.Town, 100000);
		addTrob(PartOfSpeech.County, PartOfSpeech.Province, 100);
		addTrob(PartOfSpeech.County, PartOfSpeech.Street, 100000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.District, 10000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.County, -900000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.Other, -1000);
		addTrob(PartOfSpeech.County, PartOfSpeech.Unknow, -10000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.End, 10000);
		addTrob(PartOfSpeech.County, PartOfSpeech.LandMark, 1000000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.SuffixDistrict, -1000000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.City, -1000000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.SuffixCity, -1000000000);

		addTrob(PartOfSpeech.RelatedPos, PartOfSpeech.County, -1000);
		addTrob(PartOfSpeech.RelatedPos, PartOfSpeech.LandMark, 100000);
		addTrob(PartOfSpeech.RelatedPos, PartOfSpeech.End, 10);
		addTrob(PartOfSpeech.RelatedPos, PartOfSpeech.Municipality, 100);
		addTrob(PartOfSpeech.Street, PartOfSpeech.SuffixLandMark, 100);

		addTrob(PartOfSpeech.Town, PartOfSpeech.Village, 10000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.LandMark, 10000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.District, 10000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.Street, 100000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.SuffixLandMark, 10000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.End, 100);
		addTrob(PartOfSpeech.Town, PartOfSpeech.Town, 1);
		addTrob(PartOfSpeech.Town, PartOfSpeech.RelatedPos, 10000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.Village, 100000000);
		addTrob(PartOfSpeech.Town, PartOfSpeech.SuffixCounty, -100000000);
		addTrob(PartOfSpeech.StreetNo, PartOfSpeech.BuildingUnit, 10);
		addTrob(PartOfSpeech.StreetNo, PartOfSpeech.LandMark, 10000000);
		addTrob(PartOfSpeech.StreetNo, PartOfSpeech.BuildingNo, 100);
		addTrob(PartOfSpeech.StreetNo, PartOfSpeech.City, 10000);
		addTrob(PartOfSpeech.No, PartOfSpeech.District, 10000000);
		addTrob(PartOfSpeech.No, PartOfSpeech.RelatedPos, 10000);
		addTrob(PartOfSpeech.No, PartOfSpeech.LandMark, 10000000);
		addTrob(PartOfSpeech.No, PartOfSpeech.Street, 10000000);
		addTrob(PartOfSpeech.No, PartOfSpeech.City, -10000000);
		addTrob(PartOfSpeech.No, PartOfSpeech.SuffixBuildingUnit, 10000000);

		addTrob(PartOfSpeech.Street, PartOfSpeech.City, -1000000000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.County, -10000000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.Town, -1000000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.Street, 1000000000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.SuffixStreet, 10000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.LandMark, 10000000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.No, 10000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.End, 1000000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.City, -1000000);
		addTrob(PartOfSpeech.Village, PartOfSpeech.District, 1000);
		addTrob(PartOfSpeech.Village, PartOfSpeech.End, 100);
		addTrob(PartOfSpeech.Village, PartOfSpeech.Street, 100);
		addTrob(PartOfSpeech.Village, PartOfSpeech.LandMark, 100);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.No, 10000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.End, 10000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.Street, 10000000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.StartSuffix, 10000000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.RelatedPos, 100000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.City, -900000000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.County, -900000000);

		addTrob(PartOfSpeech.BuildingUnit, PartOfSpeech.District, 100000);
		addTrob(PartOfSpeech.BuildingUnit, PartOfSpeech.Province, 10);
		addTrob(PartOfSpeech.BuildingUnit, PartOfSpeech.Street, 10);
		addTrob(PartOfSpeech.District, PartOfSpeech.LandMark, 1000000);
		addTrob(PartOfSpeech.District, PartOfSpeech.City, -10000);
		addTrob(PartOfSpeech.District, PartOfSpeech.Village, 1000000);
		addTrob(PartOfSpeech.District, PartOfSpeech.Street, 1000000);
		addTrob(PartOfSpeech.District, PartOfSpeech.County, -1000000);
		addTrob(PartOfSpeech.District, PartOfSpeech.District, 1000000);
		addTrob(PartOfSpeech.District, PartOfSpeech.End, 1000000);
		addTrob(PartOfSpeech.SuffixBuildingNo, PartOfSpeech.County, 10000);
		addTrob(PartOfSpeech.IndicationFacility, PartOfSpeech.IndicationPosition,
				10000);
		addTrob(PartOfSpeech.IndicationFacility, PartOfSpeech.RelatedPos,
				100000000);
		addTrob(PartOfSpeech.IndicationPosition, PartOfSpeech.LandMark, 100000000);
		addTrob(PartOfSpeech.Unknow, PartOfSpeech.SuffixLandMark, 100000);
		addTrob(PartOfSpeech.Unknow, PartOfSpeech.Street, 100000);
		addTrob(PartOfSpeech.Unknow, PartOfSpeech.SuffixStreet, 100000);
		addTrob(PartOfSpeech.Unknow, PartOfSpeech.City, -1000000000);

		addTrob(PartOfSpeech.SuffixLandMark, PartOfSpeech.No, 100000);
		addTrob(PartOfSpeech.SuffixLandMark, PartOfSpeech.RelatedPos, 100000);
		addTrob(PartOfSpeech.SuffixLandMark, PartOfSpeech.County, -10000);

		addTrob(PartOfSpeech.County, PartOfSpeech.SuffixLandMark, -10000);
		addTrob(PartOfSpeech.County, PartOfSpeech.County, -10000);
		addTrob(PartOfSpeech.County, PartOfSpeech.SuffixCounty, -100000);
		addTrob(PartOfSpeech.Municipality, PartOfSpeech.SuffixCity, -100000);
		addTrob(PartOfSpeech.Municipality, PartOfSpeech.SuffixStreet, -100000);
		addTrob(PartOfSpeech.Municipality, PartOfSpeech.City, -1000000000);
		addTrob(PartOfSpeech.County, PartOfSpeech.SuffixStreet, -100000);
		addTrob(PartOfSpeech.Unknow, PartOfSpeech.Town, -100000);
		addTrob(PartOfSpeech.Unknow, PartOfSpeech.County, -100000);
		addTrob(PartOfSpeech.Other, PartOfSpeech.LandMark, 100000000);

		addTrob(PartOfSpeech.SuffixCity, PartOfSpeech.SuffixLandMark, -100000);
		addTrob(PartOfSpeech.City, PartOfSpeech.SuffixCity, -100000);
		addTrob(PartOfSpeech.No, PartOfSpeech.SuffixLandMark, 100000);

		addTrob(PartOfSpeech.SuffixDistrict, PartOfSpeech.County, -1000);
		addTrob(PartOfSpeech.Street, PartOfSpeech.County, -100000000);
		addTrob(PartOfSpeech.SuffixCity, PartOfSpeech.Street, 1000000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.Town, 1000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.Street, 1000000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.County, -100000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.City, -10000000);
		addTrob(PartOfSpeech.SuffixStreet, PartOfSpeech.City, -100000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.Village, 100000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.Street, 10000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.District, 100000000);
		addTrob(PartOfSpeech.SuffixCounty, PartOfSpeech.LandMark, 1000000000);
		addTrob(PartOfSpeech.SuffixTown, PartOfSpeech.Town, -100000000);
		addTrob(PartOfSpeech.SuffixTown, PartOfSpeech.County, -100000000);
		addTrob(PartOfSpeech.SuffixTown, PartOfSpeech.Village, 100000000);
		addTrob(PartOfSpeech.SuffixTown, PartOfSpeech.RelatedPos, -10000000);
		addTrob(PartOfSpeech.SuffixTown, PartOfSpeech.City, -100000);

		addTrob(PartOfSpeech.LandMark, PartOfSpeech.SuffixCounty, -900000000);
		addTrob(PartOfSpeech.LandMark, PartOfSpeech.SuffixCity, -900000000);
		addTrob(PartOfSpeech.SuffixLandMark, PartOfSpeech.County, -100000);
		addTrob(PartOfSpeech.SuffixLandMark, PartOfSpeech.Street, 10000000);
		addTrob(PartOfSpeech.StartSuffix, PartOfSpeech.LandMark, 20000);
		addTrob(PartOfSpeech.StartSuffix, PartOfSpeech.Town, -10000000);*/
    }

    public void load(String fileName){
        int tableLen;
        BufferedReader br= null;
        String line;

        int totalFreq=0;
        int[] posFreq = new int[PartOfSpeech.values().length];
        int[][] contextFreq=new int[PartOfSpeech.values().length][PartOfSpeech.values().length];

        try {
			/*InputStream file = null;
			if (System.getProperty("dic.dir") == null)
			{
				//file = getClass().getResourceAsStream(Dictionary.getDir()+sFilename);
			}
			else
			{
				file = new FileInputStream(new File(fileName));
			}*/
            FileReader filereader = new FileReader(fileName);
            br = new BufferedReader(filereader);
            //fp = new BufferedReader(new InputStreamReader(file,"GBK"));
        }
        catch (Exception e) {
            System.err.println("FileNotFoundException:" + e);
            return ;
        }

        try {
            //	read the table length
            tableLen = Integer.parseInt(br.readLine());
            System.out.println("table length:"+tableLen);

            System.out.println("POS length:"+PartOfSpeech.values().length);
            //	new buffer for symbol
            //HashMap<PartOfSpeech,Integer> symbolTable=new HashMap<PartOfSpeech,Integer>();
            int[] symbolTable = new int[PartOfSpeech.values().length];

            //ArrayList<Integer> symbolList = new ArrayList<Integer>(50);
            //Read the context

            int nTotalFreq=0;
            for(int i=0 ;i<tableLen;i++)
            {
                line = br.readLine();
                StringTokenizer st = new StringTokenizer(line,":" );
                int key=Integer.parseInt(st.nextToken());
                int val=Integer.parseInt(st.nextToken());//the every POS frequency
                System.out.println("POS key:"+key);
                System.out.println("POS name:"+POSNameMap.get((short)key));
                PartOfSpeech.valueOf(POSNameMap.get((short) key));
                //symbolTable[]
                //symbolTable.put(key, val);
                nTotalFreq += val;
                //System.out.println("POS frequency:"+symbolTable.vals[i]);
                //symbolList.add(key);
            }

			/*int[][] aContextArray=new int[tableLen][tableLen];
	        for(int i=0;i<tableLen;i++)
			{
	        	line = fp.readLine();
	        	StringTokenizer st = new StringTokenizer(line," " );
			    for(int j=0 ;j<tableLen;j++)
			    {
			    	aContextArray[i][j]=Integer.parseInt(st.nextToken());
			    }
			}*/

            //possibilities = new LexialPossibility(aContextArray,symbolList,symbolTable,nTotalFreq);
            //calcPossibility(aContextArray,symbolList,nTotalFreq);

        }
        catch(EOFException e){
            e.printStackTrace(System.err);
        }
        catch(IOException e){
            e.printStackTrace(System.err);
        }

        try{
            br.close();
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
    }

    public void addTrob(PartOfSpeech prev, PartOfSpeech cur, int val) {
        transProbs[prev.ordinal()][cur.ordinal()] = val;
    }

    /**
     * get context possibility
     *
     * @param nPrev
     *            the previous POS
     * @param nCur
     *            the current POS
     * @return the context possibility between two POSs
     */
    public int getContextPossibility(PartOfSpeech prev, PartOfSpeech cur) {
        return transProbs[prev.ordinal()][cur.ordinal()];
    }

    public static int[][] getTransProbs() {
        return transProbs;
    }
}
