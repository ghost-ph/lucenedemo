package com.patterncat.lucene.statemachine;

public class Guard {

    private InputEvent event;
    private String label = "";

    Guard() {
    }

    public void setEvent(InputEvent event) {
        this.event = event;
    }

    public InputEvent getEvent() {
        return event;
    }

    public boolean checkEvent(InputEvent e) {
        if (e == null) {
            return false;
        }
        if (event == null) {
            return false;
        }
        return (event.equals(e));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Guard)) {
            return false;
        }
        Guard other = (Guard) obj;
        return event.equals(other.event);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.event != null ? this.event.hashCode() : 0);
        return hash;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String l) {
        this.label = l;
    }

    @Override
    public String toString() {
        return label;
    }
}

