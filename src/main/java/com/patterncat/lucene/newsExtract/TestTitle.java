package com.patterncat.lucene.newsExtract;

import org.cyberneko.html.parsers.DOMParser;

public class TestTitle {

    public static void main(String[] argv) throws Exception {
        DOMParser parser = new DOMParser(); // 创建DOM解析器
        parser.parse("http://www.gongkong.com/Common/Details.aspx?c=1&m=7&l=8&Type=news&CompanyID=0-8F38-217A2BF31D51&Id=2011042111562900003"); // 解析网页
        //NewsInfo n = Extractor.extractAll(parser.getDocument());// 从根节点开始遍历

        String title = Extractor.getClearTitle(parser.getDocument());
        System.out.println(title);
    }
}
