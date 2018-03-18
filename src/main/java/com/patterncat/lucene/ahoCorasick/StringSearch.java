package com.patterncat.lucene.ahoCorasick;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class StringSearch {
    // 树节点
    private final class TreeNode {
        public TreeNode(TreeNode parent, char c) {
            _char = c;
            _parent = parent;
            _results = new ArrayList<String>();// 存储所有没有重复的模式串的数组

            _transitionsAr = new TreeNode[] {};
            _transHash = new Hashtable<Character, TreeNode>();
        }

        // 将模式串中不在_results中的模式串添加进来
        public void addResult(String result) {
            if (_results.contains(result))// //如果已经包含该模式则不增加到模式串中
                return;
            _results.add(result);
        }

        public void addTransition(TreeNode node) {// 增加一个孩子节点
            _transHash.put(node._char, node);
            TreeNode[] ar = new TreeNode[_transHash.size()];
            Iterator<TreeNode> it = _transHash.values().iterator();
            for (int i = 0; i < ar.length; i++) {
                if (it.hasNext()) {
                    ar[i] = it.next();
                }
            }
            _transitionsAr = ar;
            //System.out.println("_transitionsAr.length:"+_transitionsAr.length)
            // ;
        }

        public TreeNode getTransition(char c) {
            return _transHash.get(c);
        }

        public boolean containsTransition(char c) {
            return getTransition(c) != null;
        }

        private char _char;// 节点代表的字符
        private TreeNode _parent;// 该节点的父节点
        private TreeNode _failure;// 匹配失败后跳转的节点
        private ArrayList<String> _results;// 存储模式串的数组变量
        private TreeNode[] _transitionsAr;
        private Hashtable<Character, TreeNode> _transHash;// 存储孩子节点的哈希表

        public char getChar() {
            return _char;
        }

        public TreeNode parent() {
            return _parent;
        }

        public TreeNode failure(TreeNode value) {
            _failure = value;
            return _failure;

        }

        public TreeNode[] transitions() {

            return _transitionsAr;
        }

        public ArrayList<String> results() {
            return _results;
        }

        public String getPath()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(_char);
            TreeNode parent = _parent;
            if(parent==null)
            {
                return sb.reverse().toString();
            }
            while (parent._parent!=null)
            {
                sb.append(parent._char);
                parent = parent._parent;
            }
            return sb.reverse().toString();
        }
    }

    private TreeNode _root;

    public StringSearch(String[] keywords) {
        buildTree(keywords);//构建树
        addFailure();//增加失败匹配属性
    }

    private void buildTree(String[] _keywords) {
        _root = new TreeNode(null, ' ');//根节点

        for (String p : _keywords) {//遍历模式串中的单词
            //System.out.println("p:" + p);
            TreeNode nd = _root;

            for (char c : p.toCharArray()) {
                // 按树的层次创建节点
                TreeNode ndNew = null;
                for (TreeNode trans : nd.transitions())
                    if (trans.getChar() == c) {
                        ndNew = trans;
                        break;
                    }

                if (ndNew == null) {
                    ndNew = new TreeNode(nd, c);
                    nd.addTransition(ndNew);
                }
                nd = ndNew;
            }
            nd.addResult(p);//把模式串记录到匹配结果
        }
    }

    private void addFailure()
    {
        //所有词的第n个字节点的集合，n从2开始
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();

        //所有词的第2个字节点的集合
        for (TreeNode nd : _root.transitions()) {
            nd.failure(_root);
            for (TreeNode trans : nd.transitions())
            {
                nodes.add(trans);
                System.out.println("add:"+trans.getPath());
            }
        }

        //所有词的第n+1个字节点的集合
        while (nodes.size() != 0) {
            ArrayList<TreeNode> newNodes = new ArrayList<TreeNode>();
            for (TreeNode nd : nodes) {
                TreeNode r = nd.parent()._failure;
                char c = nd.getChar();

                //如果在父节点的失败节点的孩子节点中没有同样字符结尾的，则继续在失败节点的失败节点中找
                while (r != null && !r.containsTransition(c))
                    r = r._failure;

                if (r == null)
                    nd._failure = _root;
                else {
                    System.out.println("r:"+r.getPath());
                    nd._failure = r.getTransition(c);
                    System.out.println("nd"+nd.getPath()+" nd._failure:" + nd._failure.getPath());
                    for (String result : nd._failure.results()) {
                        System.out.println("nd._failure result:" + result);
                        nd.addResult(result);
                    }
                }
                for (TreeNode child : nd.transitions())
                {
                    //System.out.println("add:"+child.getPath());
                    newNodes.add(child);
                }
            }
            nodes = newNodes;
        }
        _root._failure = _root;
    }

    public StringSearchResult[] findAll(String text) {
        ArrayList<StringSearchResult> ret = new ArrayList<StringSearchResult>();
        TreeNode ptr = _root;
        int index = 0;

        while (index < text.length()) {
            // System.out.println("index:" + index);
            TreeNode trans = null;
            while (trans == null) {
                trans = ptr.getTransition(text.charAt(index));

                if (ptr == _root)
                    break;
                if (trans == null) {
                    if (ptr._failure != null) {
                        System.out.println("ptr._failure:" + ptr._failure._char);
                    }
                    ptr = ptr._failure;
                }
                // else
                // System.out.println("trans:" + trans._char);
            }
            if (trans != null)
                ptr = trans;

            System.out.println("size:" + ptr.results().size());
            for (String found : ptr.results())
            {
                System.out.println("found:" + found);
                ret.add(new StringSearchResult(index - found.length() + 1,
                        found));
            }
            index++;
        }

        return ret.toArray(new StringSearchResult[ret.size()]);
    }

    public StringSearchResult findFirst(String text) {
        TreeNode ptr = _root;
        int index = 0;

        while (index < text.length()) {
            TreeNode trans = null;
            while (trans == null) {
                trans = ptr.getTransition(text.charAt(1));

                if (ptr == _root)
                    break;
                if (trans == null)
                    ptr = ptr._failure;
            }
            if (trans != null)
                ptr = trans;

            for (String found : ptr.results())
                return new StringSearchResult(index - found.length() + 1, found);
            index++;
        }
        return null;
    }

    public boolean containsAny(String text) {
        TreeNode ptr = _root;
        int index = 0;

        while (index < text.length()) {
            TreeNode trans = null;
            while (trans == null) {
                trans = ptr.getTransition(text.charAt(index));
                if (ptr == _root)
                    break;
                if (trans == null)
                    ptr = ptr._failure;
            }
            if (trans != null)
                ptr = trans;

            if (ptr.results().size() > 0)
                return true;
            index++;
        }
        return false;
    }

    /*
     *深度遍历
     */
    public void depthSearch(TreeNode node, StringBuilder ret,int deapth) {
        if (node != null) {
            for (int i = 0; i < deapth; i++)
                ret.append("|  ");
            ret.append("|——");
            ret.append(node._char + "\n");
            for (TreeNode child : node.transitions()) {
                int childDeapth = deapth + 1;//计算深度并赋值
                depthSearch(child, ret, childDeapth);
            }
        }
    }

    /*
     * 打印树节点
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("打印树节点：\n");
        int deapth = 0;
        depthSearch(_root, ret,deapth);
        return ret.toString();
    }
}
