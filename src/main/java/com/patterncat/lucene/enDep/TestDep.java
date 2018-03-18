package com.patterncat.lucene.enDep;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class TestDep {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //a book 分词得到的词序列
        ArrayList<Token> seg = new ArrayList<Token>();
        seg.add(new Token("a",0,1));
        seg.add(new Token("book",1,5));

        // a book 的 依存文法树
        // a -det-> book
        ArrayList<ArrayList<TreeNode>> depTree = new ArrayList<ArrayList<TreeNode>>();

        TreeNode book = new TreeNode(seg.get(1));
        TreeNode a = new TreeNode(seg.get(0));
        a.dominator = book; //“本”的支配词是“书”
        a.relation = GrammaticalRelation.DETERMINER;

        TreeNode root = book;

        ArrayList<TreeNode> firstLevel = new ArrayList<TreeNode>();
        firstLevel.add(a);
        firstLevel.add(book);
        depTree.add(firstLevel);

        String sent= getSentence(root,depTree);
        System.out.println(sent);
    }

    // 从依存文法树的根节点生成句子
    static String getSentence(TreeNode root,ArrayList<ArrayList<TreeNode>> depTree){

        ArrayList<TermNode> result = new ArrayList<TermNode>();
        result.add(new TermNode(root));

        ArrayDeque<TreeInf> queue = new ArrayDeque<TreeInf>();
        queue.add(new TreeInf(root,0));
        //int level = 0;

        for (TreeInf headNode = queue.poll();headNode!=null;headNode = queue.poll()) {
            ArrayList<TreeNode> currentLevel = depTree.get(headNode.level);
            int head = currentLevel.indexOf(headNode.n);

            //System.out.println(headNode);
            //System.out.println(head);
            //System.out.println("current level:"+headNode.level);

            int resultPos = result.indexOf(new TermNode(headNode.n));

            for (int k = 0; k <head; ++k) {
                TreeNode currentNode = currentLevel.get(k);
                result.add(resultPos, new TermNode(currentNode));
                if(currentNode.child!=null){
                    queue.add(new TreeInf(currentNode.child,headNode.level+1));
                }
                ++resultPos;
            }

            for (int k = (head + 1); k < currentLevel.size(); ++k) {
                TreeNode currentNode = currentLevel.get(k);
                result.add(resultPos, new TermNode(currentNode));
                if(currentNode.child!=null){
                    queue.add(new TreeInf(currentNode.child,headNode.level+1));
                }
                ++resultPos;
            }
        }

        StringBuilder sb = new StringBuilder();
        for(TermNode n:result){
            sb.append(n.term);
        }
        return sb.toString();
    }
}
