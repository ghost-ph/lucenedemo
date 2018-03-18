package com.patterncat.lucene.bigramSeg;

public class TestAdjList {

    public static void main(String[] args) {
        Segmenter seg = new Segmenter();
        String sentence="中国成立了";;
        AdjList g = seg.getSegGraph(sentence);

        System.out.println(g.toString());

        for(CnToken currentWord: g){
            System.out.println(currentWord);
        }

    }

}
