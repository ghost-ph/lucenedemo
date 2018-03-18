package com.patterncat.lucene.synonym;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.CharsRef;

public class SynonymAnalyzerTest {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String text = "The quick brown fox jumps over the lazy dog";

        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        builder.add(new CharsRef("fast"), new CharsRef("quick"), true);
        builder.add(new CharsRef("jumps"), new CharsRef("hops"), true);
        SynonymMap map = builder.build();

        SynonymAnalyzer analyzer = new SynonymAnalyzer(map);

        TokenStream stream = analyzer.tokenStream("title", new StringReader(
                text));
        stream.reset();  //重置TokenStream
        // 增加Token表示的字符串属性
        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);

        while (stream.incrementToken()) {
            System.out.println(term + " "+ type.type()); // 逐个单词输出
        }
        analyzer.close();

    }


}

