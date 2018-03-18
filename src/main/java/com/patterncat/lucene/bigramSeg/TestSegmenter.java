package com.patterncat.lucene.bigramSeg;

import java.util.ArrayDeque;

/**
 * @author luogang
 * @2013-8-5
 */
public class TestSegmenter {

    public static void main(String[] args) {
        testPath();
    }

    public static void testSeg(){
        String sentence = "中国成立了";// "大学生活动中心";
        Segmenter seg = new Segmenter();
        ArrayDeque<CnToken> words = seg.split(sentence);
        for (CnToken word : words) {
            System.out.print(word.termText + " ");
            System.out.print(' ');
        }
    }

    public static void testPath() {
        Segmenter seg = new Segmenter();
        seg.startNode = new CnToken(-1, 0, "start");

        CnToken w1 = new CnToken(0,1, "有");
        w1.bestPrev = seg.startNode;

        CnToken w2 = new CnToken(1, 3, "意见");
        w2.bestPrev = w1;

        CnToken w3 = new CnToken(3, 5, "分歧");
        w3.bestPrev = w2;

        seg.endNode = new CnToken(5, 6, "end");
        seg.endNode.bestPrev = w3;

        ArrayDeque<CnToken> words = seg.bestPath();
        for (CnToken word : words) {
            System.out.print(word.termText + " ");
            System.out.print(' ');
        }
    }

}
