package com.patterncat.lucene.synonym;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.util.BytesRef;

public class SynonymPayloadFilter extends TokenFilter {
    private final PayloadAttribute attr = addAttribute(PayloadAttribute.class);

    protected SynonymPayloadFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        BytesRef p = new BytesRef(PayloadHelper.encodeInt(1));
        attr.setPayload(p);

		/*boolean hasNext = ts.incrementToken();
        if(hasNext) {
            termAtt.append("test");
            payAtt.setPayload(new Payload(item.getBytes()));
        }
        return hasNext;*/

        return false;
    }

}

