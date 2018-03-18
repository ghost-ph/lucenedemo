package com.patterncat.lucene.tbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Trie tree Node
 * @author luogang
 *
 */
public class TrieNode{
    private PartOfSpeech nodeKey;
    private ArrayList<PartOfSpeech> nodeValue;
    private boolean terminal;
    private Map<PartOfSpeech, TrieNode> children = new HashMap<PartOfSpeech, TrieNode>();

    public PartOfSpeech getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(PartOfSpeech c) {
        this.nodeKey = c;
    }

    public ArrayList<PartOfSpeech> getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(ArrayList<PartOfSpeech> nodeValue) {
        this.nodeValue = nodeValue;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Map<PartOfSpeech, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(Map<PartOfSpeech, TrieNode> children) {
        this.children = children;
    }

    public String toString()
    {
        return String.valueOf(nodeKey);
    }
}


