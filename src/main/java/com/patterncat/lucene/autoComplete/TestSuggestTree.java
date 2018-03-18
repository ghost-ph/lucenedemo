package com.patterncat.lucene.autoComplete;

public class TestSuggestTree {
    public static void main(String[] args) {
        SuggestTree suggestTree = new SuggestTree(2);

        String[] suggestionsInAlphabeticalOrder = {"ham","hammer","hammock"};
        String[] suggestionsInRankOrder = {"ham","hammer","hammock"};
        suggestTree.build(suggestionsInAlphabeticalOrder,
                suggestionsInRankOrder);

        System.out.println("size:"+suggestTree.size());
        String[] completions = suggestTree.bestCompletions("h");
        for(String word : completions)
        {
            System.out.println(word);
        }
    }
}
