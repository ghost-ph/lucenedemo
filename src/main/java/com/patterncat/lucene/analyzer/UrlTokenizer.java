package com.patterncat.lucene.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
/**
 * @author Administer
 * @see
 * @2013-8-3
 */
public class UrlTokenizer  extends Tokenizer{

    /**
     * @param factory
     * @param input
     */
    protected UrlTokenizer(AttributeFactory factory, Reader input) {
        super(factory, input);
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public boolean incrementToken() throws IOException {
        return false;
    }

}
