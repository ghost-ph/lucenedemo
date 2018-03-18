package com.patterncat.lucene.parser;

/**
 * The grammar is a concrete implementation of a context-free grammar ({@link Grammar}). It has the following productions:
 *
 *  <ul>
 *  <li>S NP VP</li>
 *  <li>NP -&gt; Adj NP</li>
 *  <li>NP -&gt; Noun PP (NP -&gt; NP PP)</li>
 *  <li>NP -&gt; Det Noun</li>
 *  <li>NP -&gt; Noun</li>
 *  <li>VP -&gt; Verb PP</li>
 *  <li>VP -&gt; Verb NP</li>
 *  <li>VP -&gt; Verb</li>
 *  <li>PP -&gt; Prep NP</li>
 *
 *  <li>Noun -&gt; time</li>
 *  <li>Adj -&gt; time</li>
 *  <li>Verb -&gt; time</li>
 *  <li>Noun -&gt; flies</li>
 *  <li>Verb -&gt; flies</li>
 *  <li>Verb -&gt; like</li>
 *  <li>Prep -&gt; like</li>
 *  <li>Det -&gt; an</li>
 *  <li>Noun -&gt; arrow</li>
 *  </ul>
 *
 *  S is the start symbol, and the grammar recognises the sentence &quot;time flies like an arrow&quot;. The grammar has been changed to remove left recursion (NP -&gt; Noun PP (NP -&gt; NP PP)).
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-13
 *
 */
public class TimeFliesGrammar extends Grammar{

    /**
     * Creates a grammar, for the sentence &quot;time flies like an arrow&quot;.
     */
    public TimeFliesGrammar() {
        super();
        createGrammar();
    }

    /* (non-Javadoc)
	 * @see parsing.Grammar#createGrammar()
	 */
    public Grammar createGrammar() {
        // load grammar
        // create non-terminals
        NonTerminal s = new NonTerminal("S");
        NonTerminal np = new NonTerminal("NP");
        NonTerminal vp = new NonTerminal("VP");
        NonTerminal adj = new NonTerminal("Adj");
        NonTerminal noun = new NonTerminal("Noun");
        NonTerminal pp = new NonTerminal("PP");
        NonTerminal det = new NonTerminal("Det");
        NonTerminal verb = new NonTerminal("Verb");
        NonTerminal prep = new NonTerminal("Prep");

        // create terminals
        Terminal time = new Terminal("time");
        Terminal flies = new Terminal("flies");
        Terminal like = new Terminal("like");
        Terminal an = new Terminal("an");
        Terminal arrow = new Terminal("arrow");


        setStartSymbol(s);

        //		S -> NP VP
        addNonTerminalProduction(s, new Symbol[]{np, vp});

        //		NP -> Adj NP
        addNonTerminalProduction(np, new Symbol[]{adj, np});

        //		NP -> Noun PP (NP -> NP PP)
        addNonTerminalProduction(np, new Symbol[]{noun, pp});

        //		NP -> Det Noun
        addNonTerminalProduction(np, new Symbol[]{det, noun});

        //		NP -> Noun
        addNonTerminalProduction(np, new Symbol[]{noun});

        //		VP -> Verb PP
        addNonTerminalProduction(vp, new Symbol[]{verb, pp});

        //		VP -> Verb NP
        addNonTerminalProduction(vp, new Symbol[]{verb, np});

        //		VP -> Verb
        addNonTerminalProduction(vp, new Symbol[]{verb});

        //		PP -> Prep NP
        addNonTerminalProduction(pp, new Symbol[]{prep, np});


        //		time : Noun, Adj, Verb
        addTerminalProduction(noun, time);
        addTerminalProduction(adj, time);
        addTerminalProduction(verb, time);

        //		flies : Noun, Verb
        addTerminalProduction(noun, flies);
        addTerminalProduction(verb, flies);

        //		like : Verb, Prep
        addTerminalProduction(verb, like);
        addTerminalProduction(prep, like);

        //		an : Det
        addTerminalProduction(det, an);

        //		arrow : Noun
        addTerminalProduction(noun, arrow);

        return this;
    }

}

