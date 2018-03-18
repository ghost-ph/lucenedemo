package com.patterncat.lucene.trie;

import java.io.*;
import java.util.ArrayList;

/**
 * User: luogang
 * Date: 2010-4-29
 * Time: 13:06:26
 */
public class TelTrieSearch {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        TelTrieSearch ts = TelTrieSearch.getInstance();
        //TrieNodeSearch ts = TrieNodeSearch.getInstance();
        System.out.println(ts.toString());

        String searchString = "037163949884";

        System.out.println((System.currentTimeMillis() - startTime) + "ms||" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "m");
        try {
            startTime = System.currentTimeMillis();
            for(int k=0;k<10000;++k)
            {
                ts.search(searchString);
            }

            System.out.println(ts.search("071877"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() - startTime) + "ms||" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "m");

    }

    static TelTrieSearch INSTANCE;
    static String dicPath;

    public static TelTrieSearch getInstance() {
        if (null == INSTANCE) {
            try
            {
                INSTANCE = new TelTrieSearch();
            }
            catch(Exception e)
            {}
        }
        return INSTANCE;
    }

    public TrieNode root;

    private TelTrieSearch() throws Exception {
        this.root = new TrieNode('0');
        //
        if (null == dicPath)
            load("C:/Program Files/eclipse/workspace/POI/dic/telcode.txt");
        else
            load(dicPath);
    }

    //从文本文件加载词库
    public void load(String dicPath) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dicPath), "gbk"));
        String data;
        while ((data = br.readLine()) != null) {
            String[] strs = data.trim().split(":");
            String area = strs[1] + ":" + strs[2];
            addWord(strs[0], root, area);
        }
        br.close();
    }

    //把词加入词典
    private void addWord(String word, TrieNode root, String area) throws Exception {
        TrieNode currentNode = root;
        for (int i = 1; i < word.length(); i++) {
            char c0 = word.charAt(i);
            int ind = Integer.parseInt(word.substring(i, i + 1));
            if (null == currentNode.children[ind]) {
                currentNode.children[ind] = new TrieNode(c0);
            }
            currentNode = currentNode.children[ind];
        }
        currentNode.area = area;
    }

    // 这里应该自定义个异常
    public String search(String tel) throws Exception {
        TrieNode tstNode = root;
        for (int i = 1; i < tel.length(); i++) {
            tstNode = tstNode.children[(tel.charAt(i)-'0')];
            if (null != tstNode.area) {
                return tstNode.area;
            }
        }
        return null;//没找到
    }

    public static final class TrieNode {
        /**
         * The relative nodes.
         */
        protected TrieNode[] children;

        /**
         * The char used in the split.
         */
        protected char splitChar;

        /**
         * storage 区位信息
         */
        protected String area;

        /**
         * Constructor method.
         *
         * @param splitchar The char used in the split.
         */
        protected TrieNode(char splitchar) {
            children = new TrieNode[10];
            area = null;
            this.splitChar = splitchar;
        }

        public String getPath(ArrayList<TrieNode> parentNodes)
        {
            StringBuilder sb = new StringBuilder();
            for(TrieNode p : parentNodes)
            {
                sb.append(p.splitChar);
            }
            sb.append(this.splitChar);
            return sb.toString();
        }
    }

    public void deepSearch(ArrayList<TrieNode> parentNodes,TrieNode node, StringBuilder ret, int deapth) {
        if (node != null) {
            for (int i = 0; i < deapth; i++)
                ret.append("|  ");
            ret.append(node.getPath(parentNodes) + "\n");

            ArrayList<TrieNode> newParent = (ArrayList<TrieNode>) parentNodes.clone();
            newParent.add(node);
            for(TrieNode c:node.children)
            {
                deepSearch(newParent,c, ret, deapth + 1);
            }
        }
    }

    // 打印树状图行
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ArrayList<TrieNode> parentNodes = new ArrayList<TrieNode>();
        deepSearch(parentNodes,root, ret, 0);
        return ret.toString();
    }
}

