package com.patterncat.lucene.probSeg;

public class CalcProb {

    /**
     * @param args
     */
    public static void main(String[] args) {
        double logs1 = Math.log(0.018)+Math.log(0.001)+Math.log(0.0001);
        System.out.println(logs1);

        double logs2 = Math.log(0.005)+Math.log(0.002)+Math.log(0.0001);
        System.out.println(logs2);
    }

}
