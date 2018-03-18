package com.patterncat.lucene.ahoCorasick;

public class Test {
    public static void main(String[] args) {
        String [] strs={"北京","北","北极","北纬","北京行空","北京行天","北京行海","北大","北海","印度","印度尼西亚","度过","中国人民银行","人民银行","银行","严厉"};
        SearchTrie sch=SearchTrie.getInit(strs);
        //SearchTrie sch=SearchTrie.getInstance();
        //从武汉来到

        System.out.println(sch.toString());
        //StringSearchResult[] resultStr=sch.findAll("北京在印度度过中国人民银行长期");
        //for(StringSearchResult str : resultStr){
        //	System.out.println(str.index()+"："+str.keyword());
        //}
    }
}
