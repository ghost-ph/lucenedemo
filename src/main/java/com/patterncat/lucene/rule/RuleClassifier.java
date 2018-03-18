package com.patterncat.lucene.rule;

import com.patterncat.lucene.bayes.FMMSegment;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 基于规则的分类器
 */
public class RuleClassifier
{
    private static FMMSegment spliter = new FMMSegment();
    private static ArrayList<Rule> decisionList = new ArrayList<Rule>();//有序的规则集合

    /**
     * 默认的构造器，初始化规则集
     */
    public RuleClassifier()
    {
        Rule r = new Rule(new String[]{"涨停"},"财经");
        decisionList.add(r);

        r = new Rule(new String[]{"比赛"},"体育");
        decisionList.add(r);
    }

    /**
     * 对给定的文本进行分类
     * @param text 给定的文本
     * @return 分类结果
     */
    public String classify(String text)
    {
        ArrayList<String> terms = spliter.getWords(text);//中文分词处理
        HashSet<String> instance = new HashSet<String>();
        instance.addAll(terms);

        for (int i = 0; i < decisionList.size(); i++) {
            Rule rule = decisionList.get(i);
            if (rule.covers(instance))
                return rule.consequent;
        }
        return "";
    }

    public static void main(String[] args)
    {
        String text ="NBA比赛";
        RuleClassifier classifier = new RuleClassifier();//构造规则分类器
        String result = classifier.classify(text);//进行分类
        System.out.println("此项属于["+result+"]类别");
    }
}
