package com.patterncat.lucene.seg;

public class TestSegmenter {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Segmenter seg = new Segmenter("掱大学生活动中心"); //切分文本
        String word;
        do {
            word = seg.nextWord(); //返回一个词
            System.out.println(word);
        } while (word != null);
    }

}

