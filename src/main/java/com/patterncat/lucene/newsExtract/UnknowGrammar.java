package com.patterncat.lucene.newsExtract;

import java.util.ArrayList;

/**
 * 未登录地名识别规则
 *
 * @author luogang
 * @2010-3-23
 */
public class UnknowGrammar {

    /**
     * An inner class of Ternary Search Trie that represents a node in the trie.
     */
    public final class TSTNode {

        /** The key to the node. */
        public ArrayList<DocSpan> data = null;

        /** The relative nodes. */
        protected TSTNode loKID;
        protected TSTNode eqKID;
        protected TSTNode hiKID;

        /** The char used in the split. */
        protected DocType splitchar;

        /**
         * Constructor method.
         *
         *@param splitchar
         *            The char used in the split.
         */
        protected TSTNode(DocType splitchar) {
            this.splitchar = splitchar;
        }

        public String toString() {
            return "splitchar:" + splitchar;
        }
    }

    /** The base node in the trie. */
    public TSTNode root;

    public void addProduct(ArrayList<DocType> key,
                           ArrayList<DocSpan> lhs) {
        if (root == null) {
            root = new TSTNode(key.get(0));
        }
        TSTNode node = null;
        if (key.size() > 0 && root != null) {
            TSTNode currentNode = root;
            int charIndex = 0;
            while (true) {
                if (currentNode == null)
                    break;
                int charComp = key.get(charIndex).compareTo(
                        currentNode.splitchar);
                if (charComp == 0) {
                    charIndex++;
                    if (charIndex == key.size()) {
                        node = currentNode;
                        break;
                    }
                    currentNode = currentNode.eqKID;
                } else if (charComp < 0) {
                    currentNode = currentNode.loKID;
                } else {
                    currentNode = currentNode.hiKID;
                }
            }
            ArrayList<DocSpan> occur2 = null;
            if (node != null) {
                occur2 = node.data;
            }
            if (occur2 != null) {
                // occur2.insert(pi);
                return;
            }
            currentNode = getOrCreateNode(key);
            currentNode.data = lhs;
        }
    }

    public MatchRet matchLong(ArrayList<DocToken> key, int offset,
                              MatchRet matchRet) {

        if (key == null || root == null || "".equals(key)
                || offset >= key.size()) {
            matchRet.end = offset;
            matchRet.lhs = null;
            return matchRet;
        }
        int ret = offset;
        ArrayList<DocSpan> retPOS = null;

        // System.out.println("enter");
        TSTNode currentNode = root;
        int charIndex = offset;
        while (true) {
            if (currentNode == null) {
                // System.out.println("ret "+ret);
                matchRet.end = ret;
                matchRet.lhs = retPOS;
                return matchRet;
            }
            int charComp = key.get(charIndex).type
                    .compareTo(currentNode.splitchar);

            if (charComp == 0) {
                // System.out.println("comp:"+key.get(charIndex).type);
                charIndex++;

                if (currentNode.data != null && charIndex > ret) {
                    ret = charIndex;
                    retPOS = currentNode.data;
                    // System.out.println("ret pos:"+retPOS);
                }
                if (charIndex == key.size()) {
                    matchRet.end = ret;
                    matchRet.lhs = retPOS;
                    return matchRet;
                }
                currentNode = currentNode.eqKID;
            } else if (charComp < 0) {
                currentNode = currentNode.loKID;
            } else {
                currentNode = currentNode.hiKID;
            }
        }
    }

    public static void replace(ArrayList<DocToken> key, int offset,
                               ArrayList<DocSpan> spans) {
        int j = 0;
        for (int i = offset; i < key.size(); ++i) {
            DocSpan span = spans.get(j);
            DocToken token = key.get(i);
            StringBuilder newText = new StringBuilder();
            int newStart = token.start;
            int newEnd = token.end;
            DocType newType = span.type;

            for (int k = 0; k < span.length; ++k) {
                token = key.get(i + k);
                newText.append(token.termText);
                newEnd = token.end;
            }
            DocToken newToken = new DocToken(newStart, newEnd, newText
                    .toString(), newType);

            for (int k = 0; k < span.length; ++k) {
                key.remove(i);
            }
            key.add(i, newToken);
            j++;
            if (j >= spans.size()) {
                return;
            }
        }
    }

    /**
     * Returns the node indexed by key, creating that node if it doesn't exist,
     * and creating any required intermediate nodes if they don't exist.
     *
     *@param key
     *            A <code>String</code> that indexes the node that is returned.
     *@return The node object indexed by key. This object is an instance of an
     *         inner class named <code>TernarySearchTrie.TSTNode</code>.
     *@exception NullPointerException
     *                If the key is <code>null</code>.
     *@exception IllegalArgumentException
     *                If the key is an empty <code>String</code>.
     */
    protected TSTNode getOrCreateNode(ArrayList<DocType> key)
            throws NullPointerException, IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException(
                    "attempt to get or create node with null key");
        }
        if ("".equals(key)) {
            throw new IllegalArgumentException(
                    "attempt to get or create node with key of zero length");
        }
        if (root == null) {
            root = new TSTNode(key.get(0));
        }
        TSTNode currentNode = root;
        int charIndex = 0;
        while (true) {
            int charComp = key.get(charIndex).compareTo(currentNode.splitchar);
            if (charComp == 0) {
                charIndex++;
                if (charIndex == key.size()) {
                    return currentNode;
                }
                if (currentNode.eqKID == null) {
                    currentNode.eqKID = new TSTNode(key.get(charIndex));
                }
                currentNode = currentNode.eqKID;
            } else if (charComp < 0) {
                if (currentNode.loKID == null) {
                    currentNode.loKID = new TSTNode(key.get(charIndex));
                }
                currentNode = currentNode.loKID;
            } else {
                if (currentNode.hiKID == null) {
                    currentNode.hiKID = new TSTNode(key.get(charIndex));
                }
                currentNode = currentNode.hiKID;
            }
        }
    }

    public static class MatchRet {
        public int end;
        public ArrayList<DocSpan> lhs;

        public MatchRet(int e, ArrayList<DocSpan> d) {
            end = e;
            lhs = d;
        }

        public String toString() {
            return end + ":" + lhs;
        }
    }

    private UnknowGrammar() {

    }

    private static UnknowGrammar dicGrammar = new UnknowGrammar();

    /**
     *
     * @return the singleton of basic dictionary
     */
    public static UnknowGrammar getInstance() {
        return dicGrammar;
    }
}
