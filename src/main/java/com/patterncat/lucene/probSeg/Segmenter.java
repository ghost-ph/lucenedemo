package com.patterncat.lucene.probSeg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Segmenter {
    static TernarySearchTrie dict= TernarySearchTrie.getInstance(); //得到词典

    int[] prevNode;
    double[] prob;

    //计算节点i的最佳前驱节点
    void getBestPrev(AdjList g,int i)
    {
        Iterator<CnToken> it = g.getPrev(i);//得到前驱词集合，从中挑选最佳前趋词
        double maxProb = Double.NEGATIVE_INFINITY;
        int maxNode = -1;

        while(it.hasNext())
        {
            CnToken itr = it.next();
            double nodeProb = prob[itr.start]+itr.logProb;//候选节点概率
            if (nodeProb > maxProb)//概率最大的算作最佳前趋
            {
                maxNode = itr.start;
                maxProb = nodeProb;
            }
        }
        prob[i] = maxProb;//节点概率
        prevNode[i] = maxNode;//最佳前驱节点
        //System.out.println("node "+i + " best prev is"+ maxID);
    }

    //计算出最大概率的数组
    public ArrayList<Integer> maxProb(AdjList g)
    {
        prevNode = new int[g.verticesNum]; //最佳前驱节点
        prob = new double[g.verticesNum]; //节点概率
        prob[0] = 0;//节点0的初始概率是1,取log后是0

        //按节点求最佳前驱
        for (int index = 1; index < g.verticesNum; index++)
        {
            //求出最大前驱
            getBestPrev(g,index);
        }

        ArrayList<Integer> ret = new ArrayList<Integer>();
        for(int i=(g.verticesNum-1);i>0;i=prevNode[i]) // 从右向左找最佳前驱节点
        {
            ret.add(i);
        }
        return ret;
    }

    public ArrayList<Integer> split(String sentence)
    {
        int sLen = sentence.length();//字符串长度
        AdjList g = new AdjList(sLen+1);//存储所有被切分的可能的词
        int j;
        TernarySearchTrie.PrefixRet wordMatch = new TernarySearchTrie.PrefixRet();

        //生成切分词图
        for(int i=0;i<sLen;)
        {
            boolean match = dict.getMatch(sentence, i, wordMatch);//到词典中查询

            if (match)// 已经匹配上
            {
                for (WordEntry word:wordMatch.values) {
                    //System.out.println(word);
                    j = i+word.word.length();
                    double logProb = Math.log(word.freq) - Math.log(dict.n);
                    g.addEdge(new CnToken(i, j, logProb, word.word));
                }
                i=wordMatch.end;
            }
            else
            {
                j = i+1;
                double logProb = Math.log(1) - Math.log(dict.n);
                g.addEdge(new CnToken(i,j,logProb,sentence.substring(i,j)));
                i=j;
            }
        }
        System.out.println(g.toString());

        //求解
        return maxProb(g);
    }

    public static void main(String[] args) throws IOException {
        String sentence = "中国成立了";//"大学生活动中心";
        ArrayList<Integer> ret = (new Segmenter()).split(sentence);
        for(Integer word:ret)
        {
            System.out.print(word + " ");
            System.out.print(' ');
        }
    }
}
