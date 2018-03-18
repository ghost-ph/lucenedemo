package com.patterncat.lucene.probSeg;

public class TestTokenList {

    /**
     * @param args
     */
    public static void main(String[] args) {
        CnToken t1 = new CnToken(2, 3, 2.0, "见");
        CnToken t2 = new CnToken(1, 3, 3.0, "意见");
        CnTokenLinkedList tokenList = new CnTokenLinkedList();
        tokenList.put(t1);
        tokenList.put(t2);
        for(CnToken t:tokenList){
            System.out.println(t);
        }
    }
}
