package com.patterncat.lucene.sentimentMiner;

public class OpinionSpan {
    public int length;  //长度
    public OpinionType type; //类型
    public int headId=-1;  //中心语

    public OpinionSpan(int l,OpinionType t){
        length = l;
        type = t;
    }

    public String toString()
    {
        return type+":"+length;
    }
}
