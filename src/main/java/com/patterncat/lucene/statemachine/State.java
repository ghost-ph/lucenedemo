package com.patterncat.lucene.statemachine;

import java.util.Collection;
import java.util.LinkedList;

public class State {
    private static int nextId = 0;
    public int id = nextId++;
    private String label = "";

    private Collection<Transition> transitions = new LinkedList<Transition>();
    private boolean finalState = false;

    public State() {
    }

    public String getLabel() {
        return label;
    }

    /**
     *
     * @return outgoing transitions
     */
    public Collection<Transition> getTransitions() {
        return transitions;
    }

    public boolean isFinal() {
        return finalState;
    }

    public void setLabel(String l) {
        this.label = l;
    }

    public void setFinal(boolean b) {
        this.finalState = b;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof State)) {
            return false;
        }
        return id == ((State) o).id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        return hash;
    }

    //public Collection<Transition> getIncomingTransitions() {
    //    return incomingTransitions;
    //}

    @Override
    public String toString() {
        return label;
    }

    public int compareTo(State o) {
        int otherid = o.id;
        if(id < otherid) return -1;
        if(id > otherid) return 1;
        return 0;
    }
}

