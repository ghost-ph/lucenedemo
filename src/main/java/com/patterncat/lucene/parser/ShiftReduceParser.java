package com.patterncat.lucene.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A shift-reduce parser for a context-free grammar ({@link Grammar}). The grammar cannot have empty productions, but any context-free grammar can be converted into an equivalent grammar without empty productions.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-14
 *
 */
public class ShiftReduceParser implements Parser {

    private Grammar grammar;
    private boolean debug = false;

    /**
     * Creates a shift-reduce parser for the given grammar.
     *
     * @param grammar A grammar without empty productions.
     */
    public ShiftReduceParser(Grammar grammar) {
        this.grammar = grammar;
    }

    /* (non-Javadoc)
     * @see parsing.Parser#recognise(parsing.NonTerminal, java.util.ArrayList)
     */
    public boolean recognise(NonTerminal category, ArrayList string) {
        Stack stack = new Stack();
        return recognise(category, string, stack);
    }

    /*
     * Shift-reduce version of recognise
     */
    private boolean recognise(NonTerminal cat, ArrayList string, Stack stack) {

        // accept?
        if (string.size() == 0
                && stack.size() == 1
                && stack.peek().equals(cat)) {
            if (debug) {
                System.err.println("Accept: recognise(" + string + ", " + stack + ")");
            }
            return true;
        }
        else if (string.size() == 0
                && stack.size() == 1 ) {
            return false;
        }

        // shift?
        if (string.size() > 0) {
            Terminal sym = (Terminal) string.get(0);
            ArrayList prods = grammar.getTerminalProductions();
            for (int i = 0; i < prods.size(); i++) {
                // is the first terminal the right-hand side of any terminal productions?
                if (((Production) prods.get(i)).getRHS().get(0).equals(sym)) {
                    // yes, so shift and check the rest
                    Symbol lhs = ((Production) prods.get(i)).getLHS();
                    ArrayList restString = new ArrayList(string);
                    restString.remove(0);
                    Stack shiftedStack = (Stack) stack.clone();
                    shiftedStack.push(lhs);
                    if (debug) {
                        System.err.println("Before shift: recognise(" + string + ", " + stack + ")");
                        System.err.println("Shifting " + string.get(0) + " and pushing " + lhs + " on stack");
                        System.err.println("After shift: recognise(" + restString + ", " + shiftedStack + ")");
                    }
                    if (recognise(cat, restString, shiftedStack)) {
                        return true;
                    }
                }
            }
        }

        // reduce?
        if (! stack.empty()) {
            ArrayList prods = (ArrayList) grammar.getRhsIndex().get(stack.peek());
            if (prods == null) {
                return false;
            }
            // do the top symbols on the stack correspond to a right-hand side of any non-terminal productions?
            for (int i = 0; i < prods.size(); i++) {
                Symbol lhs = ((Production) prods.get(i)).getLHS();
                ArrayList rhs = ((Production) prods.get(i)).getRHS();
                Stack reducedStack = (Stack) stack.clone();

                if (rhs.size() > stack.size()) {
                    continue;
                }
                ArrayList topOfStack = new ArrayList();
                for (int j = 0; j < rhs.size(); j++) {
                    topOfStack.add(reducedStack.pop());
                }
                topOfStack = reverse(topOfStack);
                if (rhs.equals(topOfStack)) {
                    // yes, so reduce by popping the right-hand side symbols and pushing the left-hand side symbol
                    reducedStack.push(lhs);
                    if (debug) {
                        System.err.println("Before reduce: recognise(" + string + ", " + stack + ")");
                        System.err.println("Pushing " + lhs + " on stack");
                        System.err.println("After reduce: recognise(" + string + ", " + reducedStack + ")");
                    }
                    // check the rest
                    if (recognise(cat, string, reducedStack)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /*
     * Reverses the order of the items in the given list.
     *
     * @param The list to reverse
     * @return The list in reverse order
     */
    private ArrayList reverse(List list) {
        ArrayList reversed = new ArrayList(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            reversed.add(list.get(i));
        }
        return reversed;
    }


    /* (non-Javadoc)
     * @see parsing.Parser#setDebug(boolean)
     */
    public void setDebug(boolean b) {
        debug = b;
    }

}
