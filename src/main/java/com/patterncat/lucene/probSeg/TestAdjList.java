package com.patterncat.lucene.probSeg;

/**
 * @author Administer
 * @see
 * @2013-7-17
 */
public class TestAdjList {

    public static void main(String[] args) {
        String sentence = "邓颖超生前使用过的物品";
        AdjList g = new AdjList(sentence .length()+1);//存储所有被切分的可能的词
        double logProb=1.0;
        //根据词典得到切分词图
        g.addEdge(new CnToken(0, 2, logProb, "邓颖"));  //增加候选人名到切分词图
        g.addEdge(new CnToken(0, 3, logProb, "邓颖超"));
    }

}

