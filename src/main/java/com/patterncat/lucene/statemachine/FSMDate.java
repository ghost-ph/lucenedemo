package com.patterncat.lucene.statemachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class FSMDate {
    private State startState; //开始状态
    private State currentState; //状态机的当前状态
    private LinkedList<State> states = new LinkedList<State>();
    private LinkedList<Guard> guards = new LinkedList<Guard>();
    ArrayList<HashMap<InputEvent,Transition>> state2Transition;

    private static FSMDate ruleFSM = new FSMDate();

    private FSMDate()
    {
        State startState = createState();
        startState.setLabel("start");

        setStartState(startState);
        reset();

        addRule();
    }

    private void addRule()
    {
        Guard numGuard = createGuard();
        numGuard.setEvent(InputEvent.yearNum);

        State yearNumState = createState();
        yearNumState.setLabel("yearNum");
        createTransition(startState, yearNumState, numGuard);

        State yearTimeState = createState();
        yearTimeState.setLabel("yearTime");

        Guard yearGuard = createGuard();
        yearGuard.setEvent(InputEvent.yearUnitEvent);

        createTransition(yearNumState, yearTimeState, yearGuard);

        Guard num2Guard = createGuard();
        num2Guard.setEvent(InputEvent.digital2);

        Guard num1Guard = createGuard();
        num1Guard.setEvent(InputEvent.digital1);

        State monthNumState = createState();
        monthNumState.setLabel("monthNum");
        createTransition(yearTimeState, monthNumState, num2Guard);
        createTransition(yearTimeState, monthNumState, num1Guard);

        State monthTimeState = createState();
        yearTimeState.setLabel("monthTime");

        Guard monthGuard = createGuard();
        monthGuard.setEvent(InputEvent.monthUnitEvent);

        createTransition(monthNumState, monthTimeState, monthGuard);

        State monthSplitState = createState();
        monthSplitState.setLabel("monthSplit");

        Guard monthSplitGuard = createGuard();
        monthSplitGuard.setEvent(InputEvent.splitEvent);

        createTransition(yearNumState, monthSplitState, monthSplitGuard);

        State monSplitNumState = createState();
        monSplitNumState.setLabel("monthSplitNum");

        createTransition(monthSplitState, monSplitNumState, num2Guard);
        createTransition(monthSplitState, monSplitNumState, num1Guard);

        State daySplitState = createState();
        daySplitState.setLabel("daySplit");

        Guard daySplitGuard = createGuard();
        daySplitGuard.setEvent(InputEvent.splitEvent);

        createTransition(monSplitNumState, daySplitState, daySplitGuard);

        State daySplitNumState = createState();
        daySplitNumState.setLabel("daySplitNum");
        daySplitNumState.setFinal(true);

        createTransition(daySplitState, daySplitNumState, num2Guard);
        createTransition(daySplitState, daySplitNumState, num1Guard);

        State dayNumState = createState();
        dayNumState.setLabel("dayNum");

        createTransition(monthTimeState, dayNumState, num2Guard);
        createTransition(monthTimeState, dayNumState, num1Guard);

        State dayTimeState = createState();
        dayTimeState.setLabel("dayTime");
        //dayTimeState.setFinal(true);

        Guard dayGuard = createGuard();
        dayGuard.setEvent(InputEvent.dayUnitEvent);

        createTransition(dayNumState, dayTimeState, dayGuard);

        State hourBlankState = createState();
        hourBlankState.setLabel("hourBlank");
        //hourBlankState.setFinal(true);

        Guard blankGuard = createGuard();
        blankGuard.setEvent(InputEvent.blankEvent);

        createTransition(dayTimeState, hourBlankState, blankGuard);

        createTransition(daySplitNumState, hourBlankState, blankGuard);

        State hourNumState = createState();
        hourNumState.setLabel("hourNum");
        //hourNumState.setFinal(true);

        Guard hourNumGuard = createGuard();
        hourNumGuard.setEvent(InputEvent.digital2);

        createTransition(hourBlankState, hourNumState, hourNumGuard);

        State colonHourState = createState();
        colonHourState.setLabel("colonHour");
        //colonHourState.setFinal(true);

        Guard colonHourGuard = createGuard();
        colonHourGuard.setEvent(InputEvent.colonEvent);

        createTransition(hourNumState, colonHourState, colonHourGuard);

        State minNumState = createState();
        minNumState.setLabel("minNum");
        minNumState.setFinal(true);

        Guard minNumGuard = createGuard();
        minNumGuard.setEvent(InputEvent.digital2);

        createTransition(colonHourState, minNumState, minNumGuard);
    }

    /**
     *
     * @return the singleton of basic dictionary
     */
    public static FSMDate getInstance()
    {
        ruleFSM.reset();
        return ruleFSM;
    }

    public void setStartState(State s) {
        this.startState = s;
    }

    /**
     * a transition strategy
     * implements first match strategy (sufficient for dfa)
     * @param e
     * @return
     */
    protected Transition selectTransition(InputEvent e) {
        System.out.println("currentState1:"+currentState);
        for (Transition t : currentState.getTransitions()) {
            Guard g = t.getGuard();
            if (g.checkEvent(e)) {
                return t;
            }
        }
        return null;
    }

    public MatchType consumeEvent(InputEvent e) {
        //System.out.println("consumeEvent");
        Transition t = selectTransition(e);
        if (t == null) {
            //System.err.println("no matching transition found");
            reset();
            return MatchType.MisMatch;
        }
        currentState = t.getNextState();
        System.out.println("currentState2:"+currentState);
        //ArrayList<TokenSpan> entryAction = currentState.getEntryAction();
        if(currentState.isFinal())
        {
            //reset();
            return MatchType.Match;
        }
        return MatchType.MatchPrefix;
    }

    public void reset() {
        currentState = startState;
    }

    /**
     * factory method
     *
     */
    public Guard createGuard() {
        Guard g = new Guard();
        guards.add(g);
        return g;
    }

    public void removeGuard(Guard g) {
        guards.remove(g);
    }

    /**
     * factory method
     */
    public void createTransition(State from, State to, Guard g) {
        Transition t = new Transition();
        t.setState(from);
        t.setNextState(to);
        from.getTransitions().add(t);
        //to.getIncomingTransitions().add(t);
        t.setGuard(g);
        //transitions.add(t);
    }

    /** creates a new Default state */
    public State createState() {
        State s = new State();
        states.add(s);
        return s;
    }

    public Collection<State> getStates() {
        return states;
    }

    public Collection<Guard> getGuards() {
        return guards;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}

