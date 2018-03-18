package com.patterncat.lucene.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * A recursive-descent parser for a context-free grammar ({@link Grammar}). The grammar cannot have left recursion, since it will generate an infinite loop, but any context-free grammar can be converted into an equivalent grammar without left-recursion.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.111
 * @version 2005-10-31
 *
 */
public class RecursiveDescentParser implements Parser {

    private Grammar grammar;
    private boolean debug = false;

    /**
     * Creates a recursive-descent parser for the given grammar.
     *
     * @param grammar A grammar without left-recursion.
     */
    public RecursiveDescentParser(Grammar grammar) {
        this.grammar = grammar;
    }

    /* (non-Javadoc)
     * @see parsing.Parser#recognise(parsing.NonTerminal, java.util.ArrayList)
     */
    public boolean recognise(NonTerminal category, ArrayList string) {
        Stack agenda = new Stack();
        agenda.push(category);
        return recognise(agenda, string);
    }


    /*
     * Recursive-descent version of recognise.
     *
     * @param agenda The symbols to look for.
     * @param string The symbol sequence to recognize.
     * @return Returns whether the symbols on the agenda were matched or not.
	 */
    private boolean recognise(Stack agenda, ArrayList string) {

        // accept?
        if (agenda.isEmpty() && string.isEmpty()) {
            if (debug) {
                System.err.println("Accept: " + agenda + ", " + string);
            }
            return true;
        }

        // scan?
        if (! agenda.isEmpty() && ! string.isEmpty() &&
                grammar.getTerminalProductions().contains(
                        new TerminalProduction(
                                (NonTerminal) agenda.peek(),
                                new ArrayList(Arrays.asList(
                                        new Symbol[]{(Symbol) string.get(0)}))))) {

            ArrayList updatedString = (ArrayList) string.clone();
            Stack updatedAgenda = (Stack) agenda.clone();
            updatedString.remove(0);
            updatedAgenda.pop();

            if (recognise(updatedAgenda, updatedString)) {
                if (debug) {
                    System.err.println("Scan: " + agenda.peek() + " -> " + string.get(0));
                    System.err.println(" before: " + agenda + ", " + string);
                    System.err.println("  after: " + updatedAgenda + ", " + updatedString);
                }
                string = updatedString;
                agenda = updatedAgenda;
                return true;
            }
        }

        // predict?
        if (! agenda.isEmpty() &&
                grammar.getLhsIndex().containsKey(agenda.peek())) {
            ArrayList prods =
                    (ArrayList) grammar.getLhsIndex().get(agenda.peek());
            for (int i = 0; i < prods.size(); i++) {
                if (prods.get(i) instanceof TerminalProduction) {
                    continue;
                }
                ArrayList rhs = ((Production) prods.get(i)).getRHS();
                Stack updatedAgenda = (Stack) agenda.clone();
                updatedAgenda.pop();
                ArrayList outString = (ArrayList) string.clone();
                for (int j = rhs.size() - 1; j >= 0; j--) {
                    updatedAgenda.push(rhs.get(j));
                }
                if (debug) {
                    System.err.println("Predict: " + agenda.peek() + " -> " + rhs);
                    System.err.println(" before: " + agenda + ", " + string);
                    System.err.println("  after: " + updatedAgenda + ", " + outString);
                }
                if (recognise(updatedAgenda, outString)) {
                    string = outString;
                    agenda = updatedAgenda;
                    return true;
                }
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see parsing.Parser#setDebug(boolean)
     */
    public void setDebug(boolean b) {
        debug = b;
    }

}
