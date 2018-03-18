package com.patterncat.lucene.poiExtract;

import java.util.ArrayList;

public class DocNode {
    public DocNode(int newStart, int newEnd, String string, DocType newType) {
        // TODO Auto-generated constructor stub
    }
    public String termText; //词
    public DocType type;    //类型
    public int start;       //开始位置
    public int end;         //结束位置
    public long cost;       //概率
    public ArrayList<DocNode> children = new ArrayList<DocNode>(); //孩子节点
}
