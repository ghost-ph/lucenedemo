package com.patterncat.lucene.cnDep;

public class TermNode {
    public String term; //词本身
    public int id; //在句子中唯一的编号

    public TermNode(TreeNode n){
        term = n.term;
        id = n.id;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TermNode)) //判断传入对象的类型
            return false;
        TermNode that = (TermNode)o;

        return (this.id == that.id);
    }
}
