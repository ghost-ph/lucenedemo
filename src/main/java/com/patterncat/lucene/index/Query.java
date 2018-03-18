package com.patterncat.lucene.index;

import java.util.ArrayList;

public class Query {
    public ArrayList<Integer> intersection(int[] docListA, int[] docListB) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        int aindex = 0;
        int bindex = 0;
        while (aindex < docListA.length && bindex < docListB.length) {
            if (docListA[aindex] == docListB[bindex]) {
                // 找到在两个数组中都出现的值
                ret.add(docListA[aindex]);
                aindex++;
                bindex++;
            } else if (docListA[aindex] < docListB[bindex]) {
                aindex++;
            } else {
                bindex++;
            }
        }
        return ret;
    }
}
