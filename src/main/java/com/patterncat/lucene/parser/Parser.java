package com.patterncat.lucene.parser;

import java.util.ArrayList;

/**
 * A parsing (sofar only recognising) interface, which the various parsers have to implement.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-13
 *
 */
public interface Parser {

/*
	public List recognise (NonTerminal category,
						   ArrayList string);
	*/

    /**
     * Tests if the left-hand side symbol (category) generates the symbol sequence (string).
     *
     * @param category The top left-hand side symbol.
     * @param string   The symbol sequence.
     * @return True if the category generates the sequence, false otherwise.
     */
    public boolean recognise (NonTerminal category,
                              ArrayList string);

    /**
     * Sets debugging output on or off.
     *
     * @param b The debug value to set.
     */
    public void setDebug(boolean b);

}
