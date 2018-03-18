package com.patterncat.lucene.classify;

import java.util.HashMap;
import java.util.Map;

/**
 * Trie tree Node
 * @author luogang
 *
 */
public class TrieNode<T> {
    private Character nodeKey;					//接点名称
    private T nodeValue;						//节点值
    private boolean terminal;					//是否结束
    private Map<Character, TrieNode<T>> children = new HashMap<Character, TrieNode<T>>();   //子节点

    public Character getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(Character nodeKey) {
        this.nodeKey = nodeKey;
    }

    public T getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(T nodeValue) {
        this.nodeValue = nodeValue;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Map<Character, TrieNode<T>> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, TrieNode<T>> children) {
        this.children = children;
    }

    public String toString()
    {
        return String.valueOf(nodeKey);
    }
}
