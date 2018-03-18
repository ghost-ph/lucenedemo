package com.patterncat.lucene.index;

/** 用于搜索的辅助类。支持对文档按相关度排序 */
public class ScoreDoc implements Comparable<ScoreDoc> {
    DocumentData doc; //文档相关的信息，包括文档编号等
    public float score; //表示这个文档和查询词有多相关

    public ScoreDoc(DocumentData d, float b) { //构造方法
        doc = d;
        score = b;
    }

    public int compareTo(ScoreDoc other) { // 按相关度排序
        // 返回值并不重要，保证需要的顺序就行
        ScoreDoc o =  other;
        return (o.score == score) ? (0) : ((score > o.score) ? (-1) : (1));
    }

    public String toString() { //格式化输出
        return doc.filename + ":\t" + doc.docid + "\n" + score;
    }
}
