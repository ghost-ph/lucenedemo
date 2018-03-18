package com.patterncat.lucene.cnDep;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class TestDep {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //分词得到的词序列
        ArrayList<Token> seg = new ArrayList<Token>();
        seg.add(new Token("一",0,1));
        seg.add(new Token("本",1,2));
        seg.add(new Token("书",2,3));

        // 一本书 的 依存文法树
        // 本 -> 书
        ArrayList<ArrayList<TreeNode>> depTree = new ArrayList<ArrayList<TreeNode>>();

        TreeNode book = new TreeNode(seg.get(2));
        TreeNode ben = new TreeNode(seg.get(1));
        ben.dominator = book; //“本”的支配词是“书”

        TreeNode root = book;

        ArrayList<TreeNode> firstLevel = new ArrayList<TreeNode>();
        firstLevel.add(ben);
        firstLevel.add(book);
        depTree.add(firstLevel);

        // 一 -> 本
        ben.child = ben.clone();

        TreeNode one = new TreeNode(seg.get(0));
        one.dominator = ben.child;  //“一”的支配词是“本”

        ArrayList<TreeNode> secondLevel = new ArrayList<TreeNode>();
        secondLevel.add(one);
        secondLevel.add(ben.child);
        depTree.add(secondLevel);

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
            System.out.println("current level:"+headNode.level);

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

