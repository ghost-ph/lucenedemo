package com.patterncat.lucene.probTagger;

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
public class DocToken {
    public String termText;
    public PartOfSpeech type;
    public int start;
    public int end;
    public long cost;
    public long code;

    public DocToken(PartOfSpeech typ) {
        type = typ;
    }

    public DocToken(int vertexFrom, int vertexTo, String word, PartOfSpeech typ) {
        start = vertexFrom;
        end = vertexTo;
        termText = word;
        type = typ;
    }

    public DocToken(int vertexFrom, int vertexTo, int c, String word, PartOfSpeech typ ,long cde) {
        start = vertexFrom;
        end = vertexTo;
        termText = word;
        cost = c;
        type = typ;
        code = cde;
    }

    public String toString() {
        // +" cost "+cost
        return "text:" + termText + " start:" + start + " end:" + end
                + " cost:" + cost + " pos:" + type +" code:"+code;
    }
}
