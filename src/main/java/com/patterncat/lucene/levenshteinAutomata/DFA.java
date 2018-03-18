package com.patterncat.lucene.levenshteinAutomata;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

/**
 * 确定有限状态机
 *
 * @author luogang
 *
 */
public class DFA {
    public static class State {
        StateSet state;

        public State(StateSet s) {
            this.state = s;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            //for (NFA.State s : state) {
            //	sb.append(s.toString() + "|");
            //}
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof State)) {
                return false;
            }
            State other = (State) obj;

            return (state.equals(other.state));
        }

        @Override
        public int hashCode() {
            return state.hashCode();
        }
    }

    public static class StackValue {
        String s;
        State s1;
        State s2;

        public StackValue(String v, State state1, State state2) {
            s = v;
            this.s1 = state1;
            this.s2 = state2;
        }
    }

    public static class StackTrieValue {
        //String s;
        State s1;
        TrieNode<String> s2;

        public StackTrieValue(State state1, TrieNode<String> state2) {
            //s = v;
            this.s1 = state1;
            this.s2 = state2;
        }

        public String toString() {
            return s1.toString() + ">>>>>" + s2.toString();
        }
    }

    private State startState; // 开始状态
    HashMap<State, HashMap<Character, State>> transitions = new HashMap<State, HashMap<Character, State>>();
    HashSet<State> finalStates = new HashSet<State>();
    HashMap<State, State> defaults = new HashMap<State, State>();

    public DFA(State s) {
        startState = s;
    }

    public Set<Character> edges(State s) {
        HashMap<Character, State> transition = transitions.get(s);
        if (transition == null)
            return null;
        return transition.keySet();
    }

    public State next(State src, char input) {
        // System.out.println("input "+input);
        HashMap<Character, State> stateTransition = transitions.get(src);
        if (stateTransition == null)
            return defaults.get(src);
        State dest = stateTransition.get(input);
        if (dest == null)
            return defaults.get(src);
        return dest;
    }

    public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
        if (setA == null)
            return null;
        Set<T> tmp = new HashSet<T>();
        for (T x : setA)
            if (setB.contains(x))
                tmp.add(x);
        return tmp;
    }

    public static ArrayList<String> intersect(DFA dfa1, DFA dfa2) {
        ArrayList<String> match = new ArrayList<String>();// 找到的正确单词集合
        Stack<StackValue> stack = new Stack<StackValue>();
        stack.add(new StackValue("", dfa1.startState, dfa2.startState));
        while (!stack.isEmpty()) {
            StackValue stackValue = stack.pop();
            Set<Character> ret = intersection(dfa1.edges(stackValue.s1), dfa2
                    .edges(stackValue.s2));
            for (char edge : ret) {
                State state1 = dfa1.next(stackValue.s1, edge);
                State state2 = dfa2.next(stackValue.s2, edge);
                if (state1 != null && state2 != null) {
                    stackValue.s += edge;
                    stack.add(new StackValue(stackValue.s, state1, state2));
                    if (dfa1.isFinal(state1) && dfa2.isFinal(state2)) {
                        match.add(stackValue.s);
                    }
                }
            }
        }
        return match;
    }

    /**
     * 有限状态转换
     * @param dfa2 正确词库
     * @return 返回相似的正确的词
     */
    public ArrayList<String> transduce(Trie<String> dfa2) {
        ArrayList<String> match = new ArrayList<String>(); //找到的正确单词集合
        ArrayDeque<StackTrieValue> stack = new ArrayDeque<StackTrieValue>();
        stack.add(new StackTrieValue(startState, dfa2.rootNode));
        while (!stack.isEmpty()) {
            StackTrieValue stackValue = stack.pop();
            // System.out.println(stackValue.toString());
            Set<Character> ret = null; //可以往前走的字符集合
            if (defaults.containsKey(stackValue.s1)) {
                ret = stackValue.s2.getChildren().keySet();
            } else {
                Set<Character> edges = edges(stackValue.s1);
                ret = intersection(edges, stackValue.s2.getChildren().keySet());
            }
            if (ret == null)
                continue;
            for (char edge : ret) {
                State state1 = next(stackValue.s1, edge);
                TrieNode<String> state2 = stackValue.s2.getChildren().get(edge);
                if (state1 != null && state2 != null) {
                    stack.add(new StackTrieValue(state1, state2));
                    if (isFinal(state1) && state2.isTerminal()) {//走到可以结束的状态
                        match.add(state2.getNodeValue());
                    }
                }
            }
        }
        return match;
    }

    private boolean isFinal(State s) {
        return finalStates.contains(s);
    }

    public void addFinalState(State newState) {
        finalStates.add(newState);
    }

    public void setDefaultTransition(State current, State newState) {
        defaults.put(current, newState);
    }

    public void addTransition(State src, char input, State dest) {
        HashMap<Character, State> transition = transitions.get(src);
        if (transition == null) {
            transition = new HashMap<Character, State>();
            transitions.put(src, transition);
        }
        transition.put(input, dest);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<State, HashMap<Character, State>> e : transitions.entrySet()) {
            sb.append(e.getKey().toString() + " " + e.getValue() + "\n");
        }
        sb.append("DFA final state\n");
        for (State s : finalStates) {
            sb.append(s + "\n");
        }

        sb.append("DFA Default Transition\n");
        for (Entry<State, State> s : defaults.entrySet()) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }

    public boolean accept(String word) {
        State currentState = startState;
        int i = 0;
        for (; i < word.length(); i++) {
            char c = word.charAt(i);
            currentState = next(currentState, c);
            if (currentState == null)
                break;
        }
        // System.out.println("currentState "+currentState);
        if (i == word.length() && isFinal(currentState))
            return true;
        return false;
    }
}

