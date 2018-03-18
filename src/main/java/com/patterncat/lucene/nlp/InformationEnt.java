package com.patterncat.lucene.nlp;

/**
 * 信息熵
 * @author luogang
 * @2013-7-14
 */
public class InformationEnt {

    public static void main(String[] args) {
        int freq1 = 5;  //第一个词出现5次
        int freq2 = 7;  //第二个词出现7次
        double temp = Math.log((double)freq1) + Math.log((double)freq2);
        int bigramCount = 3;  //共同出现3次
        int n = 100;  //文档共100个词次
        double entropy = (Math.log(n)+Math.log((double)bigramCount) - temp)/Math.log(2);//信息熵
        System.out.println(entropy);  //输出3.1
    }

}
