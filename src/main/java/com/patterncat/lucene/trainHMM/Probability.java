package com.patterncat.lucene.trainHMM;

import java.io.IOException;
import java.util.Map;

public class Probability {
    public static int length = PartOfSpeech.values().length;
    public static double [][]PROBS =new double [length][length];  //转移矩阵
    public static int [] COUNTS;
    public static Map<String,int[]> freq;
    static{
        CountTrans gen = new CountTrans();
        try {
            gen.readCorpus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i =0;i<gen.contextFreq.length;i++){
            int [] cs = gen.contextFreq[i];
            for(int ii=0;ii<cs.length;ii++){
                if(cs[ii]!=0){
                    PROBS[i][ii] = (double)cs[ii]/gen.posFreq[ii];
                }
            }
        }
        freq = gen.wordPOSFreq;
        COUNTS = gen.posFreq;
    }

}

