package com.patterncat.lucene.ahoCorasick;

public class TestSearch {
    public static void main(String[] args) {
        //String[] keyWords = //{"a", "ab", "bc", "bca", "c", "caa"};
        //{"北京","印度","印度尼西亚","度过","中国人民银行","人民","人民银行","国人民银行"};//{"aa","bb"};
        String [] keyWords={"北京","北","北极","北纬","北京行空","北京行天","北京行海","北大","北海","印度","印度尼西亚","度过","中国人民银行","人民银行","银行","严厉"};

        StringSearch searchAlg = new StringSearch(keyWords);
        //searchAlg.buildTree();
        //searchAlg.tree();

        System.out.println(searchAlg.toString());
        String textToSearch=//"abccab";
                "北京在印度度过中国人民银行长期";
        //"从武汉来到北京在印度度过中国人民银行";//"aa bb cc gg hh ff";
        // Find all matching keywords?
        StringSearchResult[] results=searchAlg.findAll(textToSearch);
        System.out.println("resultsLength="+results.length);
        // Write all results?
        for(StringSearchResult r : results)
        {
            System.out.println("Keyword='"+r.keyword()+"', Index="+r.index());
        }

    }
}
