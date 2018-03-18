package com.patterncat.lucene.autoComplete;

/**
 * An index structure for rank-ordered autocomplete suggestions. It is based on
 * a balanced ternary search tree of the suggestions. Each node of the tree
 * (each prefix of the suggestions) holds a sorted list of references to the
 * best-ranking completions. Nodes with the same completions are compressed into
 * one node.
 * <p>
 * Unlike in a regular ternary search tree, there are no characters stored at
 * the nodes. The characters are read from the referenced completions. Each node
 * just stores the length of the longest prefix represented by it.
 * <p>
 * Suggest trees are fast. Given N as the total number of suggestions, searching
 * for the completion list for a prefix of length L requires O(L + log N)
 * character comparisons. Each comparison either consumes one character of the
 * prefix or cuts the search space in half.
 * <p>
 * Suggest trees are space efficient. A tree with N suggestions has O(N) nodes.
 * For each suggestion, there is at most one leaf node added and one existing
 * node split into two nodes. Most of the nodes hold a very short completion
 * list.
 */
public class SuggestTree {

    private Node tree, newTree;
    private int topK;

    private static class Node {
        String[] completions = new String[1];
        byte length, count;
        Node left, down, right;

        Node(String suggestion) {
            completions[0] = suggestion;
            length = (byte) suggestion.length();
            count = 1;
        }

        Node(Node n) {
            completions[0] = n.completions[0];
            length = n.length;
            count = n.count;
            down = n.down;
        }
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            for(String c:completions)
            {
                sb.append(c+" ");
            }
            return sb.toString();
        }
    }

    /**
     * Creates an empty tree suggesting the <code>topK</code> best-ranking
     * completions.
     *
     * @param topK maximum length for the completion lists
     * @throws IllegalArgumentException if <code>topK</code> is less than 1 or
     * greater than 127
     */
    public SuggestTree(int topK) {
        if(topK < 1 || topK > 127)
            throw new IllegalArgumentException();
        this.topK = topK;
    }

    /**
     * Returns the best-ranking suggestions that start with the specified prefix
     * (in rank order). If no suggestion with the prefix exists or if this
     * method is called while the tree is built or rebuilt, then
     * <code>null</code> is returned.
     *
     * @param prefix the prefix
     * @return a reference to the completion list for the prefix
     * @throws IllegalArgumentException if the prefix is an empty string
     */
    public String[] bestCompletions(String prefix) {
        if(prefix.length() == 0)
            throw new IllegalArgumentException();
        Node n = tree;
        int i = 0;
        while(n != null) {
            String s = n.completions[0];
            if(s.charAt(i) > prefix.charAt(i))
                n = n.left;
            else if(s.charAt(i) < prefix.charAt(i))
                n = n.right;
            else{
                for(i++; i < n.length && i < prefix.length(); i++) {
                    if(s.charAt(i) != prefix.charAt(i))
                        return null;
                }
                if(i == prefix.length())
                    return n.completions;
                n = n.down;
            }
        }
        return null;
    }

    /**
     * Builds the tree with the specified suggestions. Both arrays must contain
     * the same suggestions. The alphabetical order is used to build a balanced
     * tree, the rank order is used to fill the completion lists.
     *
     * @param suggestionsInAlphabeticalOrder the suggestions, sorted
     * lexicographically
     * @param suggestionsInRankOrder the suggestions, sorted by rank (best
     * first)
     * @throws IllegalArgumentException if one of the suggestions is an empty
     * string or longer than 127 characters
     */
    public void build(String[] suggestionsInAlphabeticalOrder,
                      String[] suggestionsInRankOrder) {
        tree = newTree = null;
        buildBalancedTree(suggestionsInAlphabeticalOrder, 0,
                suggestionsInAlphabeticalOrder.length - 1);
        for(String s : suggestionsInRankOrder)
            fillCompletionLists(s);
        tree = newTree;
    }

    private void buildBalancedTree(String[] suggestions, int min, int max) {
        if(min <= max) {
            int mid = (min + max) / 2;
            insert(suggestions[mid]);
            buildBalancedTree(suggestions, min, mid - 1);
            buildBalancedTree(suggestions, mid + 1, max);
        }
    }

    private void insert(String suggestion) {
        if(suggestion.length() == 0 || suggestion.length() > 127)
            throw new IllegalArgumentException();
        if(newTree == null) {
            newTree = new Node(suggestion);
            return;
        }
        Node n = newTree;
        int i = 0;
        while(true) {
            String s = n.completions[0];
            if(s.charAt(i) > suggestion.charAt(i)) {
                if(n.left == null) {
                    n.left = new Node(suggestion);
                    return;
                }
                n = n.left;
            }else if(s.charAt(i) < suggestion.charAt(i)) {
                if(n.right == null) {
                    n.right = new Node(suggestion);
                    return;
                }
                n = n.right;
            }else{
                for(i++; i < n.length; i++) {
                    if(i == suggestion.length()
                            || s.charAt(i) != suggestion.charAt(i)) {
                        // split node
                        n.down = new Node(n);
                        n.length = (byte) i;
                        break;
                    }
                }
                if(n.count < topK)
                    n.count++; // count completions
                if(i == suggestion.length())
                    return;
                if(n.down == null) {
                    n.down = new Node(suggestion);
                    return;
                }
                n = n.down;
            }
        }
    }

    private void fillCompletionLists(String suggestion) {
        Node n = newTree;
        int i = 0;
        while(n != null) {
            String s = n.completions[0];
            if(s.charAt(i) > suggestion.charAt(i))
                n = n.left;
            else if(s.charAt(i) < suggestion.charAt(i))
                n = n.right;
            else{
                if(n.count > n.completions.length) {
                    // first element of a multiple element list
                    n.completions = new String[n.count];
                    n.completions[0] = suggestion;
                    n.count = 1;
                }else if(n.count < n.completions.length)
                    // subsequent elements of a multiple element list
                    n.completions[n.count++] = suggestion;
                i = n.length;
                if(i == suggestion.length())
                    return;
                n = n.down;
            }
        }
    }

    static class SizeCount {
        int nodeCount;
        int totalListLength;

        public SizeCount()
        {
            nodeCount = 0;
            totalListLength = 0;
        }

        public SizeCount(int n,int t)
        {
            nodeCount = n;
            totalListLength = t;
        }

        public String toString()
        {
            return "number of nodes: " + nodeCount + ", average list length: "
                    + (float) totalListLength / nodeCount;
        }
    }

    /**
     * Returns information about the number of nodes in the tree and the average
     * length of the completion list held by each node.
     *
     * @return information about the memory overhead of the tree (beyond the
     * size of the suggestions themselves)
     */
    public String size() {
        //int totalListLength;
        //, nodeCount, totalListLength
        //int nodeCount = totalListLength = 0;
        SizeCount sizeCount = new SizeCount();
        size(tree,sizeCount);
        return sizeCount.toString();
    }

    private void size(Node n,SizeCount sizeCount) {
        if(n != null) {
            sizeCount.nodeCount++;
            sizeCount.totalListLength += n.completions.length;
            size(n.left,sizeCount);
            size(n.down,sizeCount);
            size(n.right,sizeCount);
        }
    }


    /*private void toString(Node n,StringBuilder sb) {
        if(n != null) {
            totalListLength += n.completions.length;
            toString(n.left);
            toString(n.down);
            toString(n.right);
        }
    }*/
}
