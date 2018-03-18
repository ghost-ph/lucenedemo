package com.patterncat.lucene.tbl;

import java.util.ArrayList;
import java.util.Map;

/**
 * Trie tree
 *
 * @author luogang
 *
 */
public class RuleSet {
    public TrieNode rootNode = new TrieNode(); //根节点

    // 放入键/值对
    public void addProduct(ArrayList<PartOfSpeech> key, ArrayList<PartOfSpeech> lhs) {
        TrieNode currNode = rootNode; //当前节点
        for (int i = 0; i < key.size(); ++i) { //从前往后找键中的类型
            PartOfSpeech c = key.get(i);
            Map<PartOfSpeech, TrieNode> map = currNode.getChildren();
            currNode = map.get(c); //向下移动当前节点
            if (currNode==null) {
                currNode = new TrieNode();
                map.put(c, currNode);
            }
        }
        currNode.setTerminal(true); //设置成可以结束的节点
        currNode.setNodeValue(lhs); //设置值
    }

    // 根据键查找对应的值
    public ArrayList<PartOfSpeech> find(ArrayList<PartOfSpeech> key) {
        TrieNode currNode = rootNode; //当前节点
        for (int i = 0; i < key.size(); ++i) { //从前往后找键中的 词性
            PartOfSpeech c = key.get(i);
            currNode = currNode.getChildren().get(c);//向下移动当前节点
            if (currNode==null) {
                return null;
            }
        }
        if (currNode.isTerminal()) {
            return currNode.getNodeValue();
        }
        return null;
    }

    public RuleSet(){
        ArrayList<PartOfSpeech> lhs = new ArrayList<PartOfSpeech>(); //左边的词性序列
        ArrayList<PartOfSpeech> rhs = new ArrayList<PartOfSpeech>(); //右边的词性序列
        // m q v
        rhs.add(PartOfSpeech.m); //m
        rhs.add(PartOfSpeech.q); //q
        rhs.add(PartOfSpeech.v); //v
        // m q n
        lhs.add(PartOfSpeech.m);//m
        lhs.add(PartOfSpeech.q);//q
        lhs.add(PartOfSpeech.n);//n
        //加到规则库
        addProduct(rhs, lhs);
    }
}

