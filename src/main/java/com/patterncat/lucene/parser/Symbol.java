package com.patterncat.lucene.parser;

/**
 * The symbol (or variable) is used in productions ({@link Production}) in a context-free grammar ({@link Grammar}). It is the common supertype of non-terminals and terminals, and is therefore never instantiated.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-13
 */
public abstract class Symbol {

    private String name;


    /**
     * Creates a Symbol stub with the given name.
     *
     * @param name  The name of the symbol.
     */
    public Symbol(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the symbol.
     *
     * @return The name of the symbol.
     */
    public String getName() {
        return name;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getName().hashCode();
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public abstract boolean equals(Object obj);

}
