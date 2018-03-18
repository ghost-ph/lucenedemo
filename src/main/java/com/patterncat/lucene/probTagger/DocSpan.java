package com.patterncat.lucene.probTagger;

/**
 *
 * 用来操作AddressType.type类和并给其定义规则
 * @author Administer
 * @2010-3-18
 */

public class DocSpan {
    public int length;
    public PartOfSpeech type;

    public DocSpan(int l,PartOfSpeech t)
    {
        length = l;
        type = t;
    }

    public String toString()
    {
        return type+":"+length;
    }
}
