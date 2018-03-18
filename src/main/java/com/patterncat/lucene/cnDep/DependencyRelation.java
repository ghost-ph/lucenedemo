package com.patterncat.lucene.cnDep;

public enum DependencyRelation {
    ATT,  //定中关系(attribute)
    QUN,  //数量关系(quantity)
    ROOT,  //核心
    COO,   //并列关系(coordinate)
    APP,   //同位关系(appositive)
    LAD,   //前附加关系(left adjunct)
    RAD,   //后附加关系(right adjunct)
    VOB,    //动宾关系(verb-object)
    POB,    //介宾关系(preposition-object)
    SBV,  //主谓关系(subject-verb)
    SIM,   //比拟关系(similarity)
    VV,    //连动结构(verb-verb)
    CNJ,   //关联结构(conjunctive)
    MT,    //语态结构(mood-tense)
    IS,     //独立结构(independent structure)
    ADV,    //状中结构(adverbial)
    CMP,     //动补结构(complement)
    DE,      //“的”字结构
    DI,      //“地”字结构
    DEI,     //“得”字结构
    BA,     //“把”字结构
    BEI,    //“被”字结构
    IC,     //独立分句(independent clause)
    DC;    //依存分句(dependent clause)
}
