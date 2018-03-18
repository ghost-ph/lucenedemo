package com.patterncat.lucene.queryLexer;

import java.io.StringReader;

public class TestLexer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String strQuery = //"^19";
                //"^hfff";
                //"car AND price";
                //"car and price";
                " title:car site:http://www.lietu.com";  //查询串
        QueryParserTokenManager tokenManager = new QueryParserTokenManager(
                new FastCharStream(new StringReader(strQuery)));

        for (Token next = tokenManager.getNextToken(); !next.toString().equals("");
             next = tokenManager.getNextToken())
            System.out.println("'" + next.image + "' "+ next.kind);

    }

}
