package com.patterncat.lucene.ahoCorasick;

public class POSInf {
	public short nHandle=0;
    public int weight=0;
    public int freq;
    
    public POSInf(short handle,int w ,int f)
    {
    	nHandle=handle;
    	weight = w;
    	freq =f;
    }

    public String toString()
	{
    	return nHandle+":"+weight;
    }
}
