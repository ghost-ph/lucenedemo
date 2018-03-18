package com.patterncat.lucene.parser;

import java.util.ArrayList;

/**
 * A production is used in a context-free grammar ({@link Grammar}). The symbol ({@link NonTerminal}) on the left-hand side of the generation operator is said to generate the symbol(s) on the right-hand side.
 *
 * <p>Example: A -&gt; B C</p>
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-09
 *
 */
public abstract class Production {
    protected NonTerminal lhs;
    protected ArrayList rhs;

    /**
     * Creates a Production where lhs generates rhs (lhs -&gt; rhs<subscript>1</subscript>...rhs<subscript>n</subscript>).
     *
     * @param lhs  The symbol on the left-hand side of the generation operator.
     * @param rhs  The symbol(s) on the right-hand side of the generation operator.
     */
/*
    public Production(NonTerminal lhs, ArrayList rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	*/
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public abstract boolean equals(Object obj);

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = lhs + " ->";
        for (int i = 0; i < rhs.size(); i++) {
            s += " " + rhs.get(i);
        }
        return s;
    }

    /**
     * Returns the left-hand side symbol.
     *
     * @return The left-hand side symbol.
     */
    public NonTerminal getLHS() {
        return lhs;
    }

    /**
     * Returns the right-hand side symbol(s).
     *
     * @return The right-hand side symbol(s).
     */
    public ArrayList getRHS() {
        return rhs;
    }

}
