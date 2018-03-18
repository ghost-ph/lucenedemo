package com.patterncat.lucene.levenshteinAutomata;

import java.util.ArrayList;

/**
 *
 * @author luogang
 *
 */
public class TestDFA {

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        testNFA();
        //testDFA();
        //testMatch();
    }

    public static void testNFA() {
        NFA lev = new NFA("food",2);

        //System.out.println(lev.getStateNo(0, 0));
        System.out.println(lev.toString());
    }

    public static void testMatch(){
        //根据错误词构建NFA
        NFA lev = new NFA("fod",1);
        //把NFA转换成DFA
        DFA dfa = lev.toDFA();

        //正确词表
        Trie<String> stringTrie = new Trie<String>();
        stringTrie.add("food", "food");
        stringTrie.add("hammer", "hammer");
        stringTrie.add("hammock", "hammock");
        stringTrie.add("ipod", "ipod");
        stringTrie.add("iphone", "iphone");

        //返回相似的正确的词
        ArrayList<String> match = dfa.transduce(stringTrie);
        for(String s:match){
            System.out.println(s);
        }
    }

    public static void testDFA() {
        //构建编辑距离自动机
        NFA lev = new NFA("foxd",2);
        System.out.println(lev.toString());
        //根据幂集构造转换成确定有限状态机
        DFA dfa = lev.toDFA();
        //System.out.println("dfa");
        //System.out.println(dfa);
        //看单词food是否能够被接收
        System.out.println(dfa.accept("food"));
        System.out.println(dfa.accept("fooxd"));
        System.out.println(dfa.accept("fooxdjj"));
    }

}

