package com.patterncat.lucene.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;

public class ComTokenizer  extends Tokenizer{

    /**
     * @param factory
     * @param input
     */
    protected ComTokenizer(AttributeFactory factory, Reader input) {
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
