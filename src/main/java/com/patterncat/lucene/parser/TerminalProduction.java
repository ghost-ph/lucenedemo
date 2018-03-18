package com.patterncat.lucene.parser;

import java.util.ArrayList;

/**
 * A terminal production is used in a context-free grammar ({@link Grammar}). The symbol ({@link NonTerminal}) on the left-hand side of the generation operator is said to generate the symbol on the right-hand side ({@link Terminal}).
 *
 * <p>Example: A -&gt; b</p>
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-09
 *
 */
public class TerminalProduction extends Production {

    /**
     * Creates a TerminalProduction where lhs generates rhs (lhs -&gt; rhs).
     *
     * @param lhs  The symbol on the left-hand side of the generation operator.
     * @param rhs  The symbol on the right-hand side of the generation operator. (As the only {@link Terminal} in a symbol list.)
     */
    public TerminalProduction(NonTerminal lhs, ArrayList rhs) {
//		super(lhs, rhs);
        super();
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof TerminalProduction
                && toString().equals(obj.toString())) {
            return true;
        }
        return false;
    }

}

