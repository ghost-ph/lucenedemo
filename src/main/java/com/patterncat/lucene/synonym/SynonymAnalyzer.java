package com.patterncat.lucene.synonym;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.Version;

public class SynonymAnalyzer extends Analyzer {
    private SynonymMap engine;// 保存了一个词的同义词

    public SynonymAnalyzer(SynonymMap engine) {
        this.engine = engine;
    }

    @Override
    protected TokenStreamComponents createComponents(String arg0, Reader reader) {
        final Tokenizer source = new StandardTokenizer(Version.LUCENE_44,
                reader);// 先分词

        SynonymFilter result = new SynonymFilter(source, engine, false);// 在TokenStream中增加同义词
        return new TokenStreamComponents(source, result);
    }

}

