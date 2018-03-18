package com.patterncat.lucene.bseg;

public class TestDic {

    public static void main(String[] args) {
        String dicFile = "SDIC.txt";
        TernarySearchTrie dic=new TernarySearchTrie(dicFile);

        String sentence = "大学生活动中心";

        int offset = sentence.length()-1;
        char[] ret = dic.matchLong(sentence.toCharArray(), offset);
        System.out.print(sentence+" match:"+String.valueOf(ret));
        System.out.print(dic.numNodes());
    }

}
