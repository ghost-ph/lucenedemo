package com.patterncat.lucene.classify;

import java.util.Arrays;
import java.util.Set;

public class Categories {
    public static String catNames []; //一级分类组

    static{
        CrawlerCategory crawler = new CrawlerCategory();
        Set<String> set =crawler.readerDocument();
        catNames =new String [set.size()];
        catNames = set.toArray(catNames);
        Arrays.sort(catNames);
    }

    //创建一个隶属度数组
    public static int[] createDegress(){
        return new int[catNames.length];
    }
}
