package com.patterncat.lucene.parser;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Junit test cases for {@link ShiftReduceParser}.
 *
 * @author Eva Forsbom
 * @author evafo@stp.lingfil.uu.se
 * @version 0.1
 * @version 2005-10-14
 *
 */
public class ShiftReduceParserTest extends TestCase{

    private Grammar grammar;
    private Parser parser;
    private ArrayList sequence;
    private ArrayList falseSequence;

    protected void setUp() {
        grammar = new TimeFliesGrammar().createGrammar();
        sequence = new ArrayList(Arrays.asList(
                new Symbol[]{
                        new Terminal("time"),
                        new Terminal("flies"),
                        new Terminal("like"),
                        new Terminal("an"),
                        new Terminal("arrow")
                }));
        falseSequence = new ArrayList(Arrays.asList(
                new Symbol[]{
                        new Terminal("like"),
                        new Terminal("time"),
                        new Terminal("flies"),
                        new Terminal("an"),
                        new Terminal("arrow")
                }));
        parser = new ShiftReduceParser(grammar);
        parser.setDebug(true);
    }


    /**
     * Tests {@link parsing.ShiftReduceParser#recognise(NonTerminal, ArrayList)}.
     */
    public void testRecognise() {
        NonTerminal start = new NonTerminal("S");
        // time flies like an arrow
        boolean result = parser.recognise(start, sequence);
        Assert.assertTrue(result);

        // like flies time an arrow
        //boolean result = parser.recognise(start, falseSequence);
        //assertTrue(! result);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ShiftReduceParserTest.class);
    }
}
