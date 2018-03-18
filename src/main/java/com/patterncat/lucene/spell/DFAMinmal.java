package com.patterncat.lucene.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFAMinmal {
    /**
     * 输入一个DFA D
     * @param d DFA状态转换表
     * @param S 状态集合
     * @param E 输入字符表
     * @param s 开始状态
     * @param F 接受状态集
     * @return 一个DFA D', 它和D接受相同的语言, 且输入状态数最少
     */
    public int[][] convert(int[][] d, Set<Integer> S, char[] E, int s, Set<Integer> F)
    {
        // 首先用接受状态组和非接受状态组划分 PI
        Set<Set<Integer>> PI = new HashSet<Set<Integer>>();
        // 计算 S - F
        S.removeAll(F);
        PI.add(F);
        PI.add(S);

        // 最初, 令PInew = PI
        Set<Set<Integer>> PInew = new HashSet<Set<Integer>>();
        // TODO 解决浅复制带来的问题
        PInew.addAll(PI);

        while (true)
        {
            // 对PI中的每个组G
            for (Iterator<Set<Integer>> it = PI.iterator(); it.hasNext(); )
            {
                List<Object> groupList = new ArrayList<Object>();
                Set<Integer> G = it.next();
                // 使用字符表测试G的元素

                // 对于字符表的每个输入a
                for (int i = 0; i < E.length; i++)
                {
                    Map<Integer, Set<Integer>> groupMap = new HashMap<Integer, Set<Integer>>();
                    char a = E[i];
                    for (Iterator<Integer> sIt = G.iterator(); sIt.hasNext(); )
                    {
                        int stat = sIt.next();
                        // 从状态S出发 沿着a能够到达的状态
                        int tar = d[stat][a];
                        // 获取目标状态在PI中的位置
                        int idx = getElelementIdx(PI, tar);
                        Set<Integer> group = groupMap.get(idx);
                        if (group == null)
                        {
                            group = new HashSet<Integer>();
                            groupMap.put(idx, group);
                        }
                        group.add(stat);
                    }

                    groupList.add(groupMap);
                }

                // 在PInew中将G替换为对G进行分组得到的那些小组
                PInew.remove(G);
                PInew.addAll(setListPoly(groupList));


            }
            // 判断2个集合组是否相等
            if (!isTwoSetGrpEqual(PI, PInew))
            {
                PI.clear();
                PI.addAll(PInew);
            } else
            {
                break;
            }
        }

        // TODO 步骤4
        for (Iterator<Set<Integer>> it = PI.iterator(); it.hasNext(); )
        {
            Set<Integer> set = it.next();
            // 令S1是PI组中某个G的代表
            for (Iterator<Integer> sIt = set.iterator(); sIt.hasNext(); )
            {
                int s1 = sIt.next();
                while (sIt.hasNext())
                {
                    // 用S1替换SWP
                    int swp = sIt.next();
                    for (int i = 0; i < d.length; i++)
                    {
                        for (int j = 0; j < d[i].length; j++)
                        {
                            if (d[i][j] == swp)
                            {
                                d[i][j] = s1;
                            }
                        }
                    }
                    // 删除SWP的转换函数
                    d[swp] = new int[]{};
                }
            }

        }

        return d;
    }


    /**
     * 获取某个元素在集合组中的索引
     * @param set
     * @param element
     * @return
     */
    private int getElelementIdx(Set<Set<Integer>> set, int element)
    {
        int idx = 0;
        for (Iterator<Set<Integer>> it = set.iterator(); it.hasNext(); )
        {
            Set<Integer> g = it.next();
            if (g.contains(element))
            {
                // TODO 检查HASHCODE 是否代表了集合的位置
                return idx;
            }
            idx++;
        }
        return -1;
    }


    // 计算集合组聚合的结果
    @SuppressWarnings("unchecked")
    private Set<Set<Integer>> setListPoly(List<Object> oriSetList)
    {
        Set<Set<Integer>> result = new HashSet<Set<Integer>>();
        if (oriSetList.size() > 0)
        {
            // 读取第一个集合组
            Map<Integer, Set<Integer>> groupMap = (Map<Integer, Set<Integer>>)oriSetList.get(0);
            for (Iterator<Integer> it = groupMap.keySet().iterator(); it.hasNext(); )
            {
                result.add(groupMap.get(it.next()));
            }

            for (int i = 1; i < oriSetList.size(); i++)
            {
                // 获取中间集合
                Map<Integer, Set<Integer>> midMap = (Map<Integer, Set<Integer>>)oriSetList.get(i);
                List<Set<Integer>> midSetList = new ArrayList<Set<Integer>>();
                for (Iterator<Integer> it = midMap.keySet().iterator(); it.hasNext(); )
                {
                    midSetList.add(midMap.get(it.next()));
                }

                // 开始计算
                // 运算结果
                List<Set<Integer>> calcResult = new ArrayList<Set<Integer>>();
                for (Iterator<Set<Integer>> it = result.iterator(); it.hasNext(); )
                {
                    Set<Integer> srcSet = it.next();
                    for (int k = 0; k < midSetList.size(); k++)
                    {
                        // 计算2个集合的交集
                        Set<Integer> mixed = getSetMixed(srcSet, midSetList.get(k));
                        // 如果结果不为空
                        if (!mixed.isEmpty())
                        {
                            // 保存运算结果
                            calcResult.add(mixed);
                        }
                    }
                }

                // 将计算结果替换result
                result.clear();
                result.addAll(calcResult);
            }
        }

        return result;
    }

    // 计算二个集合的交集
    private Set<Integer> getSetMixed(Set<Integer> set1, Set<Integer> set2)
    {
        Set<Integer> mixed = new HashSet<Integer>();
        for (Iterator<Integer> it = set1.iterator(); it.hasNext(); )
        {
            int emu = it.next();
            if (set2.contains(emu))
            {
                mixed.add(emu);
            }
        }

        return mixed;
    }


    /**
     * 判断2个集合组是否相等
     * @param setGrp1
     * @param setGrp2
     * @return
     */
    private boolean isTwoSetGrpEqual(Set<Set<Integer>> setGrp1, Set<Set<Integer>> setGrp2)
    {
        boolean same = false;
        int matchCounts = 0;
        if (setGrp1.size() == setGrp2.size())
        {
            for (Iterator<Set<Integer>> it = setGrp1.iterator(); it.hasNext(); )
            {
                Set<Integer> set1 = it.next();
                for (Iterator<Set<Integer>> it2 = setGrp2.iterator(); it2.hasNext(); )
                {
                    Set<Integer> set2 = it2.next();
                    if (set2.equals(set1))
                    {
                        matchCounts++;
                    }
                }
            }

            if (matchCounts == setGrp1.size())
            {
                same = true;
            }
        }
        return same;
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // DFA的转换表
        int[][] d = {{}, {2, 3}, {2, 4}, {2, 3}, {2, 5}, {2, 3}};
        // 输入状态集合
        Set<Integer> S = new HashSet<Integer>();
        S.add(1);
        S.add(2);
        S.add(3);
        S.add(4);
        S.add(5);
        // 输入字符
        char[] E = {0, 1};
        int s = 1;
        Set<Integer> F = new HashSet<Integer>();
        F.add(5);

        DFAMinmal a339 = new DFAMinmal();
        int[][] DFA  =a339.convert(d, S, E, s, F);
        for(int i=0;i<DFA.length;++i)
        {
            for(int j=0;j<DFA[i].length;++j)
                System.out.print(DFA[i][j]+" ");
            System.out.println();
        }
    }

}

