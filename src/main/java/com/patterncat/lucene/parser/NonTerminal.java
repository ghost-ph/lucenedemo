package com.patterncat.lucene.parser;

/**
 * A symbol (or variable) used in productions ({@link Production}) in a context-free grammar ({@link Grammar}). A non-terminal is always used at the left-hand side of the generation operator, and can be used on the right-hand side in non-terminal productions ({@link NonTerminalProduction}).
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-09
 *
 */
public class NonTerminal extends Symbol {

    /**
     * Creates a NonTerminal with the given name.
     *
     * @param name  The name of the symbol.
     */
    public NonTerminal(String name) {
        super(name);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof NonTerminal
                && toString().equals(obj.toString())) {
            return true;
        }
        return false;
    }

}
