package com.patterncat.lucene.rule;

import java.util.HashSet;

/**
 * rule
 *
 * @author luogang
 */
public class Rule {
    public String[] antecedent;//关键词
    public String consequent;//分类结果

    public Rule (String[] antd,String classLabel)
    {
        antecedent = antd;
        consequent = classLabel;
    }

    /**
     * 规则是否覆盖分类文档
     *
     * @param doc 待分类实例
     * @return 如果满足分类条件，则返回true
     */
    public boolean covers(HashSet<String> doc){
        for(int i=0;i<antecedent.length;++i)
        {
            if(!doc.contains(antecedent[i]))
            {
                return false;
            }
        }
        return true;
    }
}

