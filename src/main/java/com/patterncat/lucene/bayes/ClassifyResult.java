package com.patterncat.lucene.bayes;

public class ClassifyResult
{
    public float probility;//分类的概率
    public String classification;//分类

    public ClassifyResult(String c,float p)
    {
        this.probility = p;
        this.classification = c;
    }
}
