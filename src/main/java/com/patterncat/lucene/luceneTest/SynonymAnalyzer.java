package com.patterncat.lucene.luceneTest;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.Version;

public class SynonymAnalyzer extends Analyzer {
    private SynonymMap engine;

    public SynonymAnalyzer(SynonymMap engine) {
        this.engine = engine;
    }

    @Override
    protected TokenStreamComponents createComponents(String arg0, Reader in) {
        Tokenizer source = new StandardTokenizer(Version.LUCENE_44, in);
        TokenFilter result = new SynonymFilter(source, engine, false);
        result = new StandardFilter(Version.LUCENE_44, result);
        result = new LowerCaseFilter(Version.LUCENE_44, result);
        result = new StopFilter(Version.LUCENE_44, result,
                StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        return new TokenStreamComponents(source, result);
    }

}
