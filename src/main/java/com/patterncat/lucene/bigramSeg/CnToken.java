package com.patterncat.lucene.bigramSeg;

/**
 *
 * 有向边，边的权重不能是负数。
 *
 * This class models an edge from vertex X to vertex Y -- pretty
 * straightforward.
 *
 * It is a little wasteful to keep the starting vertex, since the adjacency list
 * will do this for us -- but it makes the code neater in other places (makes
 * the Edge independent of the Adj. List
 */
public class CnToken {
    public String termText;
    public int start;
    public int end;
    public double logProb; // 词本身的概率
    public double nodeProb;// 累积概率
    public CnToken bestPrev; // 最佳前驱词

    public CnToken(int vertexFrom, int vertexTo, String word) {
        start = vertexFrom;
        end = vertexTo;
        termText = word;
    }

    public CnToken(int vertexFrom, int vertexTo, double logP, String word) {
        start = vertexFrom;
        end = vertexTo;
        termText = word;
        logProb = logP;
    }

    public String toString() {
        return "text:" + termText + " start:" + start + " end:" + end
                + " cost:" + logProb;
    }
}
