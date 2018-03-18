package com.patterncat.lucene.parser;

import java.util.ArrayList;

/**
 * A non-terminal production is used in a context-free grammar ({@link Grammar}). The symbol ({@link NonTerminal}) on the left-hand side of the generation operator is said to generate the symbol(s) ({@link Symbol}) on the right-hand side.
 *
 * <p>Example: A -&gt; B C</p>
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-09
 *
 */
public class NonTerminalProduction extends Production {

    /**
     * Creates a TerminalProduction where lhs generates rhs (lhs -&gt; rhs<subscript>1</subscript>...rhs<subscript>n</subscript>).
     *
     * @param lhs  The symbol on the left-hand side of the generation operator.
     * @param rhs  The symbol(s) on the right-hand side of the generation operator.
     */
    public NonTerminalProduction(NonTerminal lhs, ArrayList rhs) {
//      super(lhs, rhs);
        super();
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof NonTerminalProduction
                && toString().equals(obj.toString())) {
            return true;
        }
        return false;
    }

}
