package com.patterncat.lucene.hmm;

/*description:显状态，如moo，hello等，包括start和end
 */
class Symbol {

    /*@param name "moo"等这样的字符串
     */
    public String name;

    public Symbol(String s) {
        name = s;
    }
}
