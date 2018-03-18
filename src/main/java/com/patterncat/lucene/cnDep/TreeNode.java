package com.patterncat.lucene.cnDep;

public class TreeNode {
    public int id; //在句子中唯一的编号
    public String term; //词本身
    public TreeNode dominator; //支配词
    public DependencyRelation relation; //依存关系
    public TreeNode child; //孩子

    public TreeNode(Token t) {
        term = t.termText;
        id = t.start;
    }

    public TreeNode(String t,int i) {
        term = t;
        id = i;
    }

    public TreeNode clone() {
        TreeNode clone = new TreeNode(term,id);
        return clone;
    }

    public String toString(){
        return term;
    }
}
