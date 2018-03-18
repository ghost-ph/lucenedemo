package com.patterncat.lucene.hmm;

import java.util.Hashtable;

/*description:对象State的hash存储，存储值为Integer，即State的序号编码*/
public class StateIDTable {
    Hashtable<State,Integer> table;

    public StateIDTable() {
        table = new Hashtable<State,Integer>();
    }

    public void put(State obj, int i) {
        table.put(obj, new Integer(i));
    }

    public int get(State obj) {
        Integer i = (Integer) table.get(obj);
        if (i == null) {
            return 0;
        }
        return i.intValue();
    }
}
