package com.patterncat.lucene.classify;

import java.util.List;
import java.util.Set;

/**
 * Trie tree
 *
 * @author luogang
 *
 */
public class Trie {

    public TrieNode<WordRelation> rootNode = new TrieNode<WordRelation>();

    public void add(String key, WordRelation value) {
        addNode(rootNode, key, 0, value);
    }

    public WordRelation find(String key) {
        return findKey(rootNode, key);
    }

    /**
     * 分词搜索
     *
     * @param prefix
     *            要分词的字符串
     * @return
     */
    public void analysisAll(String prefix, int[] degrees) {
        int index = 0;
        TrieNode<WordRelation> node = rootNode;
        while (index < prefix.length()) {
            char c = prefix.charAt(index);
            if (" ".equals(c) || ",".equals(c)) {
                index++;
                continue;
            }
            node = node.getChildren().get(c);
            if (node == null) {
                // 获取下一个单词首字母
                index = getMinIndex(index, prefix);
                node = rootNode;
            } else if (node.isTerminal()) {
                // 判断下一个字符是否是字母
                if (index + 1 < prefix.length()) {
                    c = prefix.charAt(index + 1);
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                        index++;
                        continue;
                    }
                }
                WordRelation wr = node.getNodeValue();
                degrees[wr.getIndex()] = degrees[wr.getIndex()]
                        + wr.getDegree();
            }
            if (index == -1) {
                return;
            }
            index++;
        }
        return;
    }

    /*
     * 取得下一个单词首字符
     */
    // TODO: 效率太低
    private static int getMinIndex(int index, String prefix) {
        // char ch [] ={'.',',',' '};
        for (; index < prefix.length(); index++) {
            char c = prefix.charAt(index);
            if (c == '.') {
                return index;
            } else if (c == ',') {
                return index;
            } else if (c == ' ') {
                return index;
            }
        }
        return index;
    }

    private WordRelation findKey(TrieNode<WordRelation> currNode, String key) {
        Character c = key.charAt(0);
        if (currNode.getChildren().containsKey(c)) {
            TrieNode<WordRelation> nextNode = currNode.getChildren().get(c);
            if (key.length() == 1) {
                if (nextNode.isTerminal()) {
                    return nextNode.getNodeValue();
                }
            } else {
                return findKey(nextNode, key.substring(1));
            }
        }

        return null;
    }

    /*
     * 递归调用 ，把key分成字符子节点加入Trie
     */
    private void addNode(TrieNode<WordRelation> currNode, String key, int pos,
                         WordRelation value) {
        Character c = key.charAt(pos);
        TrieNode<WordRelation> nextNode = currNode.getChildren().get(c);

        if (nextNode == null) {
            nextNode = new TrieNode<WordRelation>();
            nextNode.setNodeKey(c);
            if (pos < key.length() - 1) {
                addNode(nextNode, key, pos + 1, value);
            } else {
                nextNode.setNodeValue(value);
                nextNode.setTerminal(true);
            }
            currNode.getChildren().put(c, nextNode);
        } else {
            if (pos < key.length() - 1) {
                addNode(nextNode, key, pos + 1, value);
            } else {
                nextNode.setNodeValue(value);
                nextNode.setTerminal(true);
            }
        }
    }

    // 怎么遍历一个树？应该采用宽度遍历的方法
    public void findKey(List<WordRelation> si, TrieNode<WordRelation> currNode,
                        String key) {
        Character c = key.charAt(0);
        if (currNode.getChildren().containsKey(c)) {
            TrieNode<WordRelation> nextNode = currNode.getChildren().get(c);
            if (key.length() == 1) {
                addItem(si, nextNode);
            } else {
                findKey(si, nextNode, key.substring(1));
            }
        }

    }

    private List<WordRelation> addItem(List<WordRelation> si,
                                       TrieNode<WordRelation> nextNode) {
        Set<Character> cset = nextNode.getChildren().keySet();
        if (nextNode.isTerminal()) {
            si.add(nextNode.getNodeValue());
        }
        for (Character c : cset) {
            addItem(si, nextNode.getChildren().get(c));
        }
        return si;
    }

}
