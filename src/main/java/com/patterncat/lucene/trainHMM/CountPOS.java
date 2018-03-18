package com.patterncat.lucene.trainHMM;

public class CountPOS {
    int[] posTotalFreq = new int[46];

    public int addFreq(int posIndex)
    {
        if(posIndex<0)
            return -1;
        return ++posTotalFreq[posIndex];
    }

    public int getFreq(int posIndex)
    {
        return posTotalFreq[posIndex];
    }

    public String toString()
    {
        StringBuilder ret = new StringBuilder();

        for(int i=0;i<posTotalFreq.length;++i)
        {
            ret.append(CorpusToDic.getPOSFromId(i)+":"+posTotalFreq[i] +" ");
        }
        return ret.toString();
    }
}

