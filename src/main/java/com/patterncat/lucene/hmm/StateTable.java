package com.patterncat.lucene.hmm;

import java.util.Hashtable;

/*description:字符串"cow"等隐状态的hash存储，存储值为对象State*/
public class StateTable {
    Hashtable<String,State> table;

    public StateTable() {
        table = new Hashtable<String,State>();
    }

    public State get(String s) {
        s = s.toUpperCase();
        State st = table.get(s);
        if (st == null) {
            st = new State(s);
            table.put(s, st);
        }
        return st;
    }
}
