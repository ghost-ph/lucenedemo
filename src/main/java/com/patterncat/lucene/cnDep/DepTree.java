package com.patterncat.lucene.cnDep;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * 依存树
 * @author luogang
 *
 */
public class DepTree {
    public TreeNode root; //根节点
    public ArrayList<DepTree> order; //树结构
    public int maxId; //记录节点的最大编号

    public DepTree(TreeNode r) {
        root = r;
        maxId = r.id;
    }

    public DepTree(TreeNode r, ArrayList<DepTree> d) {
        root = r;
        order = d;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DepTree)) //判断传入对象的类型
            return false;
        DepTree that = (DepTree)o;

        return (this.root.id == that.root.id);
    }

    //输出结构
    public void getStructure(){
        if(order == null)
            return;

        ArrayList<TermNode> result = new ArrayList<TermNode>();
        result.add(new TermNode(root));

        ArrayDeque<DepTree> queue = new ArrayDeque<DepTree>();
        queue.add(this);

        for (DepTree headNode = queue.poll();headNode!=null;headNode = queue.poll()) {
            int head = headNode.order.indexOf(headNode);

            //System.out.print(headNode.root +" ");
            //System.out.println(head);

            for (int k = 0; k <head; ++k) {
                DepTree currentNode = headNode.order.get(k);
                System.out.println(currentNode.root + " -"+ currentNode.root.relation + "-> " + headNode.root);
                if(currentNode.order!=null){
                    queue.add(currentNode);
                }
            }

            for (int k = (head + 1); k < headNode.order.size(); ++k) {
                DepTree currentNode = headNode.order.get(k);
                System.out.println(headNode.root + " <- "+ currentNode.root.relation +"- " +currentNode.root);
                if(currentNode.order!=null){
                    queue.add(currentNode);
                }
            }
            System.out.println();
        }
    }

    // 从依存文法树的根节点生成句子
    public String toSentence(){
        if(order == null)
            return root.term;

        ArrayList<TermNode> result = new ArrayList<TermNode>();
        result.add(new TermNode(root));

        ArrayDeque<DepTree> queue = new ArrayDeque<DepTree>();
        queue.add(this);

        for (DepTree headNode = queue.poll();headNode!=null;headNode = queue.poll()) {
            int head = headNode.order.indexOf(headNode);

            //System.out.println(headNode);
            //System.out.println(head);

            int resultPos = result.indexOf(new TermNode(headNode.root));

            for (int k = 0; k <head; ++k) {
                DepTree currentNode = headNode.order.get(k);
                result.add(resultPos, new TermNode(currentNode.root));
                if(currentNode.order!=null){
                    queue.add(currentNode);
                }
                ++resultPos;
            }

            ++resultPos;
            for (int k = (head + 1); k < headNode.order.size(); ++k) {
                DepTree currentNode = headNode.order.get(k);
                result.add(resultPos, new TermNode(currentNode.root));
                if(currentNode.order!=null){
                    queue.add(currentNode);
                }
                ++resultPos;
            }
        }

        StringBuilder sb = new StringBuilder();
        for(TermNode n:result){
            sb.append(n.term +" ");
        }
        return sb.toString();
    }

    public String toString(){
        return root.toString();
    }

    public String toChildren(){
        StringBuilder sb = new StringBuilder();

        if(order!=null){
            sb.append("size:"+order.size());
            sb.append(" ");

            for(DepTree e:order){
                sb.append(e + " ");
            }
        }
        return sb.toString();
    }
}
