package com.ghostph.lucene.seg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Segmenter {

    public static final class TSTNode {
        protected TSTNode left;
        protected TSTNode mid;
        protected TSTNode right;

        public char splitChar;
        protected String nodeValue; // 节点中存储的值类型不固定

        public TSTNode(char key) {
            // System.out.println("create node:"+key);
            this.splitChar = key;
        }

        public String toString() {
            return "spliter是" + splitChar;
        }
    }

    // 创建一个词相关的节点并返回对应的叶结点
    public static TSTNode createNode(String key) {
        int charIndex = 0; // 当前要比较的字符在查询词中的位置
        char currentChar = key.charAt(charIndex); // 当前要比较的字符
        if (root == null) {
            root = new TSTNode(currentChar);
        }
        TSTNode currentNode = root;
        while (true) {
            // 比较词的当前字符与节点的当前字符
            int compa = currentChar - currentNode.splitChar;
            if (compa == 0) { // 词中的字符与节点中的字符相等
                charIndex++;
                if (charIndex == key.length()) {
                    return currentNode;
                }
                currentChar = key.charAt(charIndex);
                if (currentNode.mid == null) {
                    currentNode.mid = new TSTNode(currentChar);
                }
                currentNode = currentNode.mid; // 向下找
            } else if (compa < 0) { // 词中的字符小于节点中的字符
                if (currentNode.left == null) {
                    currentNode.left = new TSTNode(currentChar);
                }
                currentNode = currentNode.left; // 向左找
            } else { // 词中的字符大于节点中的字符
                if (currentNode.right == null) {
                    currentNode.right = new TSTNode(currentChar);
                }
                currentNode = currentNode.right; // 向右找
            }
        }
    }

    private static TSTNode root;

    static {//加载词典
        String fileName = "WordList.txt";
        try {
            FileReader filereadnew = new FileReader(fileName);
            BufferedReader read = new BufferedReader(filereadnew);
            String line;
            try {
                while ((line = read.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, "\t");
                    String key = st.nextToken();

                    TSTNode currentNode = createNode(key);
                    currentNode.nodeValue = key;
                    // System.out.println(currentNode);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                read.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    String text = null; //切分文本
    int offset; //已经处理到的位置

    public Segmenter(String text) {
        this.text = text;
        offset = 0;
    }

    public String nextWord() { //得到下一个词
        String word = null;
        if (text == null || root == null) {
            return word;
        }
        if (offset >= text.length()) //已经处理完毕-+
            return word;
        TSTNode currentNode = root;
        int charIndex = offset;
        while (true) {
            if (currentNode == null) {//已经匹配完毕
                if(word==null){ //没有匹配上，则按单字切分
                    word = text.substring(offset,offset+1);
                    offset++;
                }
                return word;
            }
            int charComp = text.charAt(charIndex) - currentNode.splitChar;

            if (charComp == 0) {
                charIndex++;

                if (currentNode.nodeValue != null) {
                    word = currentNode.nodeValue; // 候选最长匹配词
                    offset = charIndex;
                }
                if (charIndex == text.length()) {
                    return word; // 已经匹配完
                }
                currentNode = currentNode.mid;
            } else if (charComp < 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
    }
}
