package com.patterncat.lucene.spider;

import java.net.MalformedURLException;
import java.net.URL;

public class TestURL {

    /**
     * @param args
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException {
        URL fromUrl = new URL("http://www.lietu.com/news/");
        String url = "../index.html";//获得超链接的值
        String newUrl = (new URL(fromUrl, url)).toString();//转成String类型
        System.out.println(newUrl);
    }
}