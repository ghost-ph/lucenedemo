package com.patterncat.lucene.hmm;

import java.util.Hashtable;

/*description:字符串"moo"等的hash存储，存储值为对象Symbol*/
public class SymbolTable {
    /*给定字符串"moo"等，至多对应一个Symbol对象*/
    Hashtable<String,Symbol> table;

    public SymbolTable() {
        table = new Hashtable<String,Symbol>();
    }
    /*
     * @param s 字符串"moo"等
     * @return 给定字符串"moo"等,返回Symbol对象
     */
    public Symbol intern(String s) {
        s = s.toLowerCase();
        Symbol sym = table.get(s);
        if (sym == null) {
            sym = new Symbol(s);
            table.put(s, sym);
        }
        return sym;
    }
}
