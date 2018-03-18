package com.patterncat.lucene.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The grammar can store a context-free grammar. A context-free grammar consists of
 *
 *  <ul>
 *  <li>A set of terminal symbols ({@link Terminal}, represented as lower-case characters)</li>
 *  <li>A set of non-terminal symbols ({@link NonTerminal}, represented as upper-case characters)</li>
 *  <li>A start symbol (one of the non-terminal symbols, represented as S)</li>
 *  <li>A set of productions ({@link Production})</li>
 *  </ul>
 *
 * In a production, the symbol ({@link NonTerminal}) on the left-hand side of the generation operator is said to generate the symbol(s) on the right-hand side. If the production generates a single terminal, it is a terminal production ({@link TerminalProduction}), otherwise it is a non-terminal production ({@link NonTerminalProduction}).
 *
 * <p>Example: A -&gt; B C</p>
 * <p>Example: A -&gt; b</p>
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-13
 *
 */
public abstract class Grammar {

    private NonTerminal startSymbol;
    private ArrayList terminals;
    private ArrayList nonTerminals;
    private ArrayList nonTerminalProductions;
    private ArrayList terminalProductions;

    private HashMap lhsIndex;
    private HashMap rhsIndex;

    //  private boolean[][] terminalGrammar;
    //  private boolean[][][] nonTerminalGrammar;


    /**
     * Creates a Grammar stub.
     */
    public Grammar() {
        terminals = new ArrayList();
        nonTerminals = new ArrayList();
        nonTerminalProductions = new ArrayList();
        terminalProductions = new ArrayList();
        lhsIndex = new HashMap();
        rhsIndex = new HashMap();
    }


    /**
     * Returns a concrete context-free grammar.
     *
     * @return A concrete context-free grammar.
     */
    public abstract Grammar createGrammar();

    /**
     * Adds a terminal production to the grammar.
     *
     * @param lhs The left-hand side symbol of the production.
     * @param rhs The right-hand side symbol of the production.
     */
    public void addTerminalProduction(Symbol lhs, Symbol rhs) {
        // create a production
        TerminalProduction production = new TerminalProduction((NonTerminal) lhs,
                new ArrayList(Arrays.asList(new Symbol[]{rhs})));

        // add
        addProduction(production);
    }


    /**
     * Adds a non-terminal production to the grammar.
     *
     * @param lhs The left-hand side symbol of the production.
     * @param rhs The right-hand side symbols of the production.
     */
    public void addNonTerminalProduction(Symbol lhs, Symbol[] rhs) {
        // create a production
        NonTerminalProduction production = new NonTerminalProduction((NonTerminal) lhs,
                new ArrayList(Arrays.asList(rhs)));

        // add
        addProduction(production);
    }


    /**
     * Adds a production to the grammar.
     *
     * @param production A production to be added.
     */
    private void addProduction(Production production) {
        // add
        if (production instanceof NonTerminalProduction) {
            nonTerminalProductions.add(production);
        }
        else if (production instanceof TerminalProduction) {
            terminalProductions.add(production);
        }

        // index on left-hand side symbol
        NonTerminal lhs = production.getLHS();
        if(! lhsIndex.containsKey(lhs)) {
            // add key and create empty container for index
            lhsIndex.put(lhs, new ArrayList());
            // index symbol if new non-terminal
            if (! nonTerminals.contains(lhs)) {
                nonTerminals.add(lhs);
            }
        }
        ((ArrayList) lhsIndex.get(lhs)).add(production);

        // index on right-hand side
        ArrayList rhs = production.getRHS();
        for (int i = 0; i < rhs.size(); i++) {
            Symbol sym = (Symbol) rhs.get(i);
            if(! rhsIndex.containsKey(sym)) {
                // add key and create empty container for index
                rhsIndex.put(sym, new ArrayList());
                // index symbol if new terminal
                if (sym instanceof Terminal
                        && ! terminals.contains(sym)) {
                    terminals.add(sym);
                }
            }
            ((ArrayList) rhsIndex.get(sym)).add(production);
        }
    }


    /**
     * Returns a list of generated symbols for every production where lhs is the left-hand side symbol.
     *
     * @param lhs  The non-terminal symbol to generate from.
     * @return A list of generated symbols for every production where lhs is the left-hand side symbol.
     */
    public ArrayList generates(NonTerminal lhs) {
        // get all possible nonTerminalProductions with lhs as left-hand side
        ArrayList productions = (ArrayList) lhsIndex.get(lhs);
        if(! productions.isEmpty() ) {
            ArrayList result = new ArrayList(productions.size());
            Iterator iter = productions.iterator();
            int index = 0;
            // get the generation for each relevant production
            while (iter.hasNext()) {
                NonTerminalProduction prod = (NonTerminalProduction) iter.next();
                result.add(index, prod.getRHS());
                index++;
            }
            return result;
        }
        return null;
    }



    /**
     * Returns the start symbol.
     *
     * @return The start symbol.
     */
    public NonTerminal getStartSymbol() {
        return startSymbol;
    }

    /**
     * Sets the start symbol of the grammar.
     *
     * @param startSymbol The start symbol to set.
     */
    public void setStartSymbol(NonTerminal startSymbol) {
        this.startSymbol = startSymbol;
    }

    /**
     * Returns the non-terminal productions.
     *
     * @return Returns the non-terminal productions.
     */
    public ArrayList getNonTerminalProductions() {
        return nonTerminalProductions;
    }

    /**
     * Returns the terminal productions.
     *
     * @return The terminal productions.
     */
    public ArrayList getTerminalProductions() {
        return terminalProductions;
    }

    /**
     * Returns an index over the non-terminal symbols.
     *
     * @return Index from left-hand side non-terminal to productions.
     */
    public HashMap getLhsIndex() {
        return lhsIndex;
    }

    /**
     * Returns an index over the right-hand side symbols.
     *
     * @return Index from right-hand side non-terminal to productions.
     */
    public HashMap getRhsIndex() {
        return rhsIndex;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = "Start symbol: " + getStartSymbol() + "\n";

        s += "\nNon-terminal productions:\n";
        Iterator iter = getNonTerminalProductions().iterator();
        while (iter.hasNext()) {
            NonTerminalProduction nt =
                    (NonTerminalProduction) iter.next();
            s += nt + "\n";
        }

        s += "\nTerminal productions:\n";
        iter = getTerminalProductions().iterator();
        while (iter.hasNext()) {
            TerminalProduction t =
                    (TerminalProduction) iter.next();
            s += t + "\n";
        }

        return s;
    }

    /**
     * Returns the terminals of the grammar.
     *
     * @return The terminals.
     */
    public ArrayList getTerminals() {
        return terminals;
    }

    /**
     * Returns the non-terminals of the grammar.
     *
     * @return The non-terminals.
     */
    public ArrayList getNonTerminals() {
        return nonTerminals;
    }

    /* maybe
    private void createTerminalGrammar() {
        ArrayList tProds = grammar.getTerminalProductions();

        List nonTerminals = new ArrayList();
        List terminals = new ArrayList();

        Iterator iter = tProds.iterator();
        while (iter.hasNext()) {
            TerminalProduction tp =
                (TerminalProduction) iter.next();
            if (! nonTerminals.contains(tp.getLHS())) {
                nonTerminals.add(tp.getLHS());
            }
        }
    }
    */

}
