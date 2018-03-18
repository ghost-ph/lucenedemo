package com.patterncat.lucene.hmm;

import java.util.Vector;

public class SymbolList {
    Vector<Symbol> list;

    public SymbolList() {
        list = new Vector<Symbol>();
    }

    public int size() {
        return list.size();
    }
    /*把Symbol对象存储在给定下表值的列表中
     * @param index 序列表下表值， sym Symbol对象
     * @return no return value
     */
    public void set(int index, Symbol sym) {
        list.setElementAt(sym, index);
    }
    /*把Symbol对象存储在列表中
     * @param  sym Symbol对象
     * @return no return value
     */
    public void add(Symbol sym) {
        list.addElement(sym);
    }

    /*在给定下表值的列表中，返回Symbol对象
     * @param  index 序列表下表值
     * @return Symbol对象
     */
    public Symbol get(int index) {
        return (Symbol) list.elementAt(index);
    }

}
