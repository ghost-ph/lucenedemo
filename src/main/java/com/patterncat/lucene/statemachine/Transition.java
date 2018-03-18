package com.patterncat.lucene.statemachine;

public class Transition {
    private Guard guard;
    private State nextState;
    private State state;

    public Transition() {
    }

    public Guard getGuard() {
        return this.guard;
    }

    public void setGuard(Guard g) {
        this.guard = g;
    }

    public State getNextState() {
        return this.nextState;
    }

    public void setNextState(State s) {
        this.nextState = s;

    }

    public void setState(State s) {
        this.state = s;
    }

    public State getState() {
        return this.state;
    }
}

