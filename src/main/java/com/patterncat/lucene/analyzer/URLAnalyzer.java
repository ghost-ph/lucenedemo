package com.patterncat.lucene.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.AttributeSource.AttributeFactory;

/**
 * @author Administer
 * @see
 * @2013-8-3
 */
public class URLAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {
        // 以/切分Token
        final Tokenizer source = new UrlTokenizer(
                AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, reader);
        // 小写化
        TokenStream result = new LowerCaseFilter(Version.LUCENE_43, source);
        return new TokenStreamComponents(source, result);
    }
}
