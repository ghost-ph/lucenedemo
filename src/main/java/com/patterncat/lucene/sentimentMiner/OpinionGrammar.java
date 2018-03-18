package com.patterncat.lucene.sentimentMiner;

import java.util.ArrayList;

public class OpinionGrammar {

    private OpinionGrammar() {
        ArrayList<OpinionSpan> lhs = new ArrayList<OpinionSpan>();  //左侧序列
        ArrayList<OpinionType> rhs = new ArrayList<OpinionType>();  //右侧序列

        rhs.add(OpinionType.entity);
        //rhs.add(OpinionType.compare);
        rhs.add(OpinionType.entity);
        rhs.add(OpinionType.positive);
        lhs.add(new OpinionSpan(4,OpinionType.tuple ));
    }
}
