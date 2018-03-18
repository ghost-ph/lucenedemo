package com.patterncat.lucene.parser;

/**
 * A symbol (or variable) used in terminal productions ({@link TerminalProduction}) in a context-free grammar ({@link Grammar}). A terminal is always used at the right-hand side of the generation operator, and never on the left-hand side.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-09
 *
 */
public class Terminal extends Symbol {

    /**
     * Creates a Terminal with the given name.
     *
     * @param name  The name of the symbol.
     */
    public Terminal(String name) {
        super(name);
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof Terminal
                && toString().equals(obj.toString())) {
            return true;
        }
        return false;
    }

}
