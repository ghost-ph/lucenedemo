package com.patterncat.lucene.htmlParser;

import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;

public class TestHtmlParser {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Parser parser;
        String url ="http://auto.cccme.org.cn/detail.asp?id=2687";

        parser = new Parser (url);

        Lexer lexer = parser.getLexer();
        lexer.setNodeFactory(new PrototypicalNodeFactory ());
        org.htmlparser.Node node;
        while (null != (node = lexer.nextNode ()))
        {
            System.out.println(node);
        }
    }

}
