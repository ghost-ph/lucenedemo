package com.patterncat.lucene.bseg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class TernarySearchTrie {

    public final class TSTNode {
        public char[] data = null;

        protected TSTNode loNode;
        protected TSTNode eqNode;
        protected TSTNode hiNode;

        public char spliter;

        public TSTNode(char key) {
            this.spliter = key;
        }

        public String toString() {
            return "data  ��" + String.valueOf(data) + "   spliter��" + spliter;
        }

		/*public String getPath() {
			StringBuilder sb = new StringBuilder();
			sb.append(spliter);
			TSTNode parent = paNode;
			while (parent != null) {
				// System.out.println("splitchar:"+parent.spliter);
				sb.append(parent.spliter);
				parent = parent.paNode;
			}
			return sb.reverse().toString();
		}*/
    }

    public TSTNode rootNode;

    /**
     * ���شʵ� �Ӻ���ǰ
     *
     * @param fileName
     */
    public TernarySearchTrie(String fileName) {
        try {
            FileReader filereadnew = new FileReader(fileName);
            BufferedReader read = new BufferedReader(filereadnew);
            String temstr = null;
            try {
                while ((temstr = read.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(temstr, "\t");

                    String word = st.nextToken();
                    char[] key = word.toCharArray();
                    int keyLength = word.length(); // �ʳ���
                    TSTNode currentNode = creatTSTNode(key, keyLength);
                    currentNode.data = key;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // ����һ������
    public TSTNode creatTSTNode(char[] key, int keyLength)
            throws NullPointerException, IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("��ָ���쳣");
        }
        int charIndex = keyLength - 1;
        if (rootNode == null) {
            rootNode = new TSTNode(key[charIndex]);
        }

        TSTNode currentNode = rootNode;

        while (true) {
            int compa = (key[charIndex] - currentNode.spliter);
            if (compa == 0) {
                charIndex--;
                if (charIndex < 0) {
                    return currentNode;
                }
                if (currentNode.eqNode == null) {
                    currentNode.eqNode = new TSTNode(key[charIndex]);
                }
                currentNode = currentNode.eqNode;
            } else if (compa < 0) {
                if (currentNode.loNode == null) {
                    currentNode.loNode = new TSTNode(key[charIndex]);
                }
                currentNode = currentNode.loNode;
            } else {
                if (currentNode.hiNode == null) {
                    currentNode.hiNode = new TSTNode(key[charIndex]);
                }
                currentNode = currentNode.hiNode;
            }
        }
    }

    public int matchNum(int start, char[] key) {
        int i = start;
        while (i >= 0) {
            char c = key[i];
            if (c >= '0' && c <= '9') {
                --i;
            } else {
                break;
            }
        }

        return i;
    }

    public int matchEnglish(int start, char[] key) {
        int i = start;
        while (i >= 0) {
            char c = key[i];
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                --i;
            } else {
                break;
            }
        }

        return i;
    }

    /**
     * �ƥ��
     *
     * @param key
     *            �ؼ���
     * @param start
     *            ��ʼ
     * @param end
     *            ���Ƚ���
     * @return
     */
    public char[] matchLong(char[] key, int start) {
        char[] ret = null;

        int numEnd = matchNum(start, key);
        if (numEnd < start) {
            // number
            char[] num = new char[start - numEnd];
            System.arraycopy(key, numEnd + 1, num, 0, num.length);
            return num;
        }

        int englishEnd = matchEnglish(start, key);
        if (englishEnd < start) {
            // english
            char[] english = new char[start - englishEnd];
            System.arraycopy(key, englishEnd + 1, english, 0, english.length);
            return english;
        }

        TSTNode currentNode = rootNode;
        int charIndex = start;
        while (true) {
            if (currentNode == null) {
                return ret;
            }
            int charComp = key[charIndex] - currentNode.spliter;

            if (charComp == 0) {
                charIndex--;

                if (currentNode.data != null) {
                    ret = currentNode.data; // ��ѡ�ƥ����
                }
                if (charIndex < 0) {
                    return ret; // �Ѿ�ƥ����
                }
                currentNode = currentNode.eqNode;
            } else if (charComp < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
    }

    /**
     *  Returns the total number of nodes in the subtrie below and including the
     *  starting Node. The method counts nodes whether or not they have data.
     *
     *@param  startingNode  The top node of the subtrie. The node that defines the subtrie.
     *@return               The total number of nodes in the subtrie.
     */
    protected int numNodes() {
        return recursiveNodeCalculator(rootNode, 0);
    }

    /**
     *  Recursivelly visists each node to calculate the number of nodes.
     *
     *@param  currentNode  The current node.
     *@param  checkData    If true we check the data to be different of <code>null</code>.
     *@param  numNodes2    The number of nodes so far.
     *@return              The number of nodes accounted.
     */
    private int recursiveNodeCalculator(
            TSTNode currentNode,
            int numNodes2) {
        if (currentNode == null) {
            return numNodes2;
        }
        int numNodes =
                recursiveNodeCalculator(
                        currentNode.loNode,
                        numNodes2);
        numNodes =
                recursiveNodeCalculator(
                        currentNode.eqNode,
                        numNodes);
        numNodes =
                recursiveNodeCalculator(
                        currentNode.hiNode,
                        numNodes);
        numNodes++;
        return numNodes;
    }

}
