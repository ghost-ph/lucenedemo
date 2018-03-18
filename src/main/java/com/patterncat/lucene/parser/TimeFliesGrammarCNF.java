package com.patterncat.lucene.parser;

/**
 * The grammar is a concrete implementation in Chomsky normal form (CNF) of a context-free grammar ({@link Grammar}). CNF means that all non-terminal productions ({@link NonTerminalProduction}) have the format A -&gt; B C, and all terminal productions ({@link TerminalProduction}) have the format A -&gt; b. The grammar has the following productions:
 *
 *  <ul>
 *  <li>S NP VP</li>
 *  <li>NP -&gt; Adj NP</li>
 *  <li>NP -&gt; NP PP</li>
 *  <li>NP -&gt; Det Noun</li>
 *  <li>VP -&gt; Verb PP</li>
 *  <li>VP -&gt; Verb NP</li>
 *  <li>PP -&gt; Prep NP</li>
 *
 *  <li>Noun -&gt; time</li>
 *  <li>NP -&gt; time</li>
 *  <li>Adj -&gt; time</li>
 *  <li>Verb -&gt; time</li>
 *  <li>VP -&gt; time</li>
 *  <li>Noun -&gt; flies</li>
 *  <li>NP -&gt; flies</li>
 *  <li>Verb -&gt; flies</li>
 *  <li>VP -&gt; flies</li>
 *  <li>Verb -&gt; like</li>
 *  <li>VP -&gt; like</li>
 *  <li>Prep -&gt; like</li>
 *  <li>Det -&gt; an</li>
 *  <li>Noun -&gt; arrow</li>
 *  <li>NP -&gt; arrow</li>
 *  </ul>
 *
 *  S is the start symbol, and the grammar recognises the sentence &quot;time flies like an arrow&quot;.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-13
 *
 */
public class TimeFliesGrammarCNF extends Grammar{

    /**
     * Creates a grammar in Chomsky normal form, for the sentence &quot;time flies like an arrow&quot;.
     */
    public TimeFliesGrammarCNF() {
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

        //		NP -> NP PP
        addNonTerminalProduction(np, new Symbol[]{np, pp});

        //		NP -> Det Noun
        addNonTerminalProduction(np, new Symbol[]{det, noun});

        //		VP -> Verb PP
        addNonTerminalProduction(vp, new Symbol[]{verb, pp});

        //		VP -> Verb NP
        addNonTerminalProduction(vp, new Symbol[]{verb, np});

        //		PP -> Prep NP
        addNonTerminalProduction(pp, new Symbol[]{prep, np});


        //		time : Noun, NP, Adj, Verb, VP
        addTerminalProduction(noun, time);
        addTerminalProduction(np, time);
        addTerminalProduction(adj, time);
        addTerminalProduction(verb, time);
        addTerminalProduction(vp, time);

        //		flies : Noun, NP, Verb, VP
        addTerminalProduction(noun, flies);
        addTerminalProduction(np, flies);
        addTerminalProduction(verb, flies);
        addTerminalProduction(vp, flies);

        //		like : Verb, VP, Prep
        addTerminalProduction(verb, like);
        addTerminalProduction(vp, like);
        addTerminalProduction(prep, like);

        //		an : Det
        addTerminalProduction(det, an);

        //		arrow : Noun, NP
        addTerminalProduction(noun, arrow);
        addTerminalProduction(np, arrow);

        return this;
    }

}
