package com.patterncat.lucene.ahoCorasick;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchTrie {
    private static TSTNode root=null;

    private static SearchTrie searchTrie;

    public static SearchTrie getInit(String[] dicWord) {
        if (searchTrie == null) {
            return new SearchTrie(dicWord);
        }
        return searchTrie;
    }

    public static SearchTrie getInstance() {
        if (searchTrie == null) {
            return new SearchTrie("SDIC.txt");
        }
        return searchTrie;
    }

    public SearchTrie(String fileName) {
        FileReader filereader = null;
        try {
            filereader = new FileReader(fileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedReader read = new BufferedReader(filereader);
        String key = null;
        try {
            while ((key = read.readLine()) != null) {
                key = (new StringTokenizer(key)).nextToken();
                if (root == null) {
                    root = new TSTNode(key.charAt(0), null);
                }

                if (key.length() > 0 && root != null) {
                    TSTNode currentNode = creatTSTNode(key);
                    currentNode.addResult(key);// 存储模式串的数组变量
                }
            }

            // 给三叉树的根节点增加新的一个父节点
            TSTNode root_parent = new TSTNode(' ', null);
            root.ptNode = root_parent;
            root_parent.eqNode = root;
            root = root_parent;
        } catch (IOException e) {
            System.out.println(e);
        }

        addFailure();
    }

    // 创建三叉树的方法
    public SearchTrie(String[] dicWord) {
        buildTree(dicWord);
        // 添加匹配失败后，跳转的节点
        addFailure();
    }

    private void buildTree(String[] dicWord) {
        for (String key : dicWord) {
            if (root == null) {
                root = new TSTNode(key.charAt(0), null);
            }

            if (key.length() > 0 && root != null) {
                TSTNode currentNode = creatTSTNode(key);
                currentNode.addResult(key);// 存储模式串的数组变量
            }
        }
    }

    // 给每个节点添加失败的节点
    private void addFailure() {
        ArrayList<TSTNode> firstNodes = this.getEqNodes(root);
        System.out.println(firstNodes.size());
        //System.out.println("path:"+firstNodes.get(0).getPath());
        ArrayList<TSTNode> nodes = new ArrayList<TSTNode>();
        // 循环遍历与根节点的同级节点 添加失败的节点为根节点
        int i=0;
        for (TSTNode node : firstNodes) {
            System.out.println("path:"+i+":"+node.getPath());
            //node.ptNode = root; // 指定父节点
            //node._failure = null; // 指定失败后跳转的节点
            ArrayList<TSTNode> trans = this.getEqNodes(node.eqNode);
            for (TSTNode t : trans) {
                System.out.println("add:"+t.getPath());
            }
            nodes.addAll(trans);
            ++i;
        }

        while (nodes.size() != 0) {
            ArrayList<TSTNode> newNodes = new ArrayList<TSTNode>();
            for (TSTNode nd : nodes) {
                TSTNode r = nd.ptNode._failure;

                char c = nd.splitchar;
                // 如果在父节点失败后跳转的孩子节点中没有找到该节点
                while (r != null && !containsTransition(r,c))
                    r = r._failure;

                if (r == null)
                {
                    r = root;
                }
                else
                {
                    System.out.println("r:" + r.getPath());
                }

                nd._failure = getTransition(r,c);
                if(nd._failure!=null)
                {
                    System.out.println("nd"+nd.getPath()+" nd._failure:" + nd._failure.getPath());
                    for (String result : nd._failure.getResult())
                    {
                        System.out.println("nd"+nd.getPath()+" add result:"+result);
                        nd.addResult(result);
                    }
                }

                for (TSTNode child : this.getEqNodes(nd.eqNode))
                    newNodes.add(child);
            }
            nodes = newNodes;

            for (TSTNode t : nodes) {
                System.out.println(t.getPath());
            }
        }
        root._failure = null;
    }

    //创建一个结点
    public TSTNode creatTSTNode(String key)throws NullPointerException,IllegalArgumentException {
        if(key==null){
            throw new NullPointerException("空指针异常");
        }
        int charIndex = 0;
        TSTNode currentNode = root;
        if (root == null) {
            root = new TSTNode(key.charAt(0), null);
        }
        TSTNode parentNode = null; // 相对父节点
        while (true) {
            int compa = (key.charAt(charIndex) - currentNode.splitchar);
            if (compa == 0) {
                charIndex++;
                if (charIndex == key.length()) {
                    return currentNode;
                }
                if (currentNode.eqNode == null) {
                    currentNode.eqNode = new TSTNode(key.charAt(charIndex),
                            currentNode);
                }
                parentNode = currentNode;
                currentNode = currentNode.eqNode;
            } else if (compa < 0) {
                if (currentNode.loNode == null) {
                    currentNode.loNode = new TSTNode(key.charAt(charIndex),
                            parentNode);
                }
                currentNode = currentNode.loNode;
            } else {
                if (currentNode.hiNode == null) {
                    currentNode.hiNode = new TSTNode(key.charAt(charIndex),
                            parentNode);
                }
                currentNode = currentNode.hiNode;
            }
        }
    }

    public TSTNode getTransition(TSTNode currentNode, char cmpChar) {
        while (true) {
            if (currentNode == null) {
                return null;
            }
            int charComp = cmpChar - currentNode.splitchar;
            if (charComp == 0) {
                return currentNode;
            } else if (charComp < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
    }

    // 从输入文本查找关键词集合的过程
    public StringSearchResult[] findAll(String text) {
        ArrayList<StringSearchResult> ret = new ArrayList<StringSearchResult>();
        TSTNode ptr = root;
        int index = 0;

        while (index < text.length()) {
            TSTNode trans = null;
            while (trans == null) {
                trans = getTransition(ptr, text.charAt(index));
                if (ptr == null)
                    break;
                if (trans == null) {
                    // 匹配失败后，从失败属性再匹配
                    ptr = ptr._failure;
                    if(ptr!=null)
                        System.out.println("ptr:" + ptr.getPath());
                }
            }
            if (trans != null)
                ptr = trans;

            if(ptr != null)
            {
                System.out.println("size:" + ptr.getResult().size());

                for (String found : ptr.getResult())
                {
                    System.out.println("found:" + found);
                    ret.add(new StringSearchResult(index - found.length() + 1,
                            found));
                }
            }
            else
            {
                ptr = root;
            }
            index++;
        }

        return ret.toArray(new StringSearchResult[ret.size()]);
    }

    public String depthSearch(TSTNode node, StringBuilder ret, int deapth) {
        if (node != null) {
            for (int i = 0; i < deapth; i++)
                ret.append("|  ");
            ret.append(node.getPath() + "\n");

            if (node.loNode != null) {
                depthSearch(node.loNode, ret, deapth + 1);
            }
            if (node.eqNode != null) {
                depthSearch(node.eqNode, ret, deapth + 1);
            }
            if (node.hiNode != null) {
                depthSearch(node.hiNode, ret, deapth + 1);
            }
			/*ret.append("|——");
			ret.append(node.splitchar + "\n");
			if (node.loNode != null) {
				depthSearch(node.loNode, ret, deapth + 1);
			}
			if (node.eqNode != null) {
				depthSearch(node.eqNode, ret, deapth + 1);
			}
			if (node.hiNode != null) {
				depthSearch(node.hiNode, ret, deapth + 1);
			}*/
        }
        return ret.toString();
    }

    // 打印树状图行
    public String toString() {
        StringBuilder ret = new StringBuilder();
        return depthSearch(root, ret, 0);
    }

    // 根据该节点的获取与该节点同级的所有节点
    public ArrayList<TSTNode> getEqNodes(TSTNode tstNode) {
        ArrayList<TSTNode> childNodes = new ArrayList<TSTNode>();
        if (tstNode != null) {
            ArrayList<TSTNode> nodes = new ArrayList<TSTNode>();
            nodes.add(tstNode);//添加节点
            childNodes = nodes;
            while (true) {
                ArrayList<TSTNode> newlist = new ArrayList<TSTNode>();
                for (TSTNode node : nodes) {
                    if (node.loNode != null) {
                        newlist.add(node.loNode);// 添加左节点
                    }
                    if (node.hiNode != null) {
                        newlist.add(node.hiNode);// 添加右节点
                    }
                }
                nodes = newlist;
                if (newlist.size() == 0) {
                    return childNodes;
                }
                childNodes.addAll(nodes);
            }
        }
        return childNodes;
    }

    // 根据某一节点查找该节点的父节点匹配失败后的节点中是否存在该节点
    private boolean containsTransition(TSTNode tstNode, char cmpChar) {
        int charComp;
        while (true) {
            if (tstNode == null) {
                return false;
            }
            charComp = cmpChar - tstNode.splitchar;
            if (charComp == 0) {
                return true;
            } else if (charComp < 0) {
                tstNode = tstNode.loNode;
            } else {
                tstNode = tstNode.hiNode;
            }
        }
    }

}
