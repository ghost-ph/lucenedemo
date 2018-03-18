package com.patterncat.lucene.hmm;

import java.util.HashMap;

/*description:可能的状态，如cow,duck包括start和end*/
public class State {
    public String name;
    HashMap<Symbol,Double> emits;//发射概率，以ProbTable形式存储
    HashMap<State,Double> linksto;//转移概率，以ProbTable形式存储

    public State(String s) {
        name = s;
        emits = new HashMap<Symbol,Double>();
        linksto = new HashMap<State,Double>();
    }

    public void addSymbol(Symbol sym, double prob) {
        emits.put(sym, prob);
    }

    public double emitprob(Symbol sym) {
        Double val = emits.get(sym);
        if(val==null){
            return 0;
        }
        return val.doubleValue();
    }

    public void addLink(State st, double prob) {
        linksto.put(st, (Double)prob);
    }

    public double transprob(State st) {
        Double val = linksto.get(st);
        if(val==null){
            return 0;
        }
        return val.doubleValue();
    }

}

