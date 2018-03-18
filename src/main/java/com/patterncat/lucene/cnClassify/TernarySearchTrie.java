package com.patterncat.lucene.cnClassify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.StringTokenizer;

public final class TernarySearchTrie {
    public final class WordRelation {
        public String word; // 关键词
        public int[] degree; // 关联度

        public WordRelation(String word, int[] degree) {
            this.word = word;
            this.degree = degree;
        }
    }

    public final class TSTNode {
        public WordRelation data;

        protected TSTNode paNode;
        protected TSTNode loNode;
        protected TSTNode eqNode;
        protected TSTNode hiNode;

        public char spliter;

        public TSTNode(char key, TSTNode parent) {
            this.spliter = key;
            paNode = parent;
        }

        public String toString() {
            return "data  是" + data + "   spliter是" + spliter;
        }
    }

    public TSTNode rootNode;

    public TernarySearchTrie(String fileName, String[] cats) {
        try {
            InputStream file = new FileInputStream(new File(fileName));
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    file, "GBK"));

            String line;
            String cat;
            String word;
            String degree;
            String[] categories = cats;

            try {
                while ((line = read.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ":");
                    while (st.hasMoreElements()) {
                        cat = st.nextToken();
                        word = st.nextToken();
                        degree = st.nextToken();
                        int index = Arrays.binarySearch(categories, cat);

                        TSTNode currentNode = creatTSTNode(word);
                        if (currentNode.data == null) {
                            currentNode.data = new WordRelation(word,
                                    new int[categories.length]);
                        }

                        currentNode.data.degree[index] = Integer
                                .parseInt(degree);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 创建一个结点
    public TSTNode creatTSTNode(String key) throws NullPointerException,
            IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("空指针异常");
        }
        int charIndex = 0;
        if (rootNode == null) {
            rootNode = new TSTNode(key.charAt(0), null);
        }
        TSTNode currentNode = rootNode;
        while (true) {

            int compa = (key.charAt(charIndex) - currentNode.spliter);
            if (compa == 0) {
                charIndex++;
                if (charIndex == key.length()) {

                    return currentNode;
                }
                if (currentNode.eqNode == null) {
                    currentNode.eqNode = new TSTNode(key.charAt(charIndex),
                            currentNode);
                }
                currentNode = currentNode.eqNode;
            } else if (compa < 0) {
                if (currentNode.loNode == null) {
                    currentNode.loNode = new TSTNode(key.charAt(charIndex),
                            currentNode);
                }
                currentNode = currentNode.loNode;
            } else {
                if (currentNode.hiNode == null) {
                    currentNode.hiNode = new TSTNode(key.charAt(charIndex),
                            currentNode);
                }
                currentNode = currentNode.hiNode;
            }
        }
    }

    public WordRelation matchLong(String key, int offset) {
        WordRelation ret = null;
        if (key == null || rootNode == null || "".equals(key)) {
            return null;
        }

        TSTNode currentNode = rootNode;
        int charIndex = offset;
        while (true) {
            if (currentNode == null) {

                return null;
            }

            int charComp = key.charAt(charIndex) - currentNode.spliter;

            if (charComp == 0) {
                charIndex++;

                if (currentNode.data != null) {

                    ret = currentNode.data; // 候选最长匹配词

                }
                if (charIndex == key.length()) {
                    return ret; // 已经匹配完
                }
                currentNode = currentNode.eqNode;
            } else if (charComp < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
    }

}
