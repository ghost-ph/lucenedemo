package com.patterncat.lucene.htmlParser;

import java.io.IOException;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TestStringParser {

    public static void main(String[] args) throws ParserException, IOException {
        String content = "<a href=\"a.html\" class=\"title\"></a>";
        Lexer lexer = new Lexer(content); // 解析网页
        Parser parser = new Parser(lexer);
        NodeFilter filter=new AndFilter(new NodeClassFilter(LinkTag.class),
                new HasAttributeFilter("class","title"));

        NodeList nodelist=parser.extractAllNodesThatMatch(filter);
        System.out.println(nodelist.size());
        System.out.println(nodelist.elementAt(0).toHtml());
    }
}
