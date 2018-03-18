package com.patterncat.lucene.cnDep;

public class DependencyInstance {
    // LEMMA: the lemmas, or stems, e.g. "think"
    public String[] terms;

    // FINE-POS: the fine-grained part-of-speech tags, e.g."VBD"
    public PartOfSpeech[] postags;

    // FEATURES: some features associated with the elements separated by "|", e.g. "PAST|3P"
    public String[][] feats;

    // HEAD: the IDs of the heads for each element
    public int[] heads;

    // DEPREL: the dependency relations, e.g. "SUBJ"
    public String[] deprels;
}
