package com.patterncat.lucene.similarity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LCS {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str = "基认为，就塔霍人的所有要求在2014年找到解决方案令人期待，但不能为实现这个目标付出过高代价。 ";
        //"高新技术开发区北环海路128号";
        String str2 = "根据发展改革委、工业和信息化部、监察部、财政部、国土资源部、环境保护部、人民银行、质检总局、银监会、证监会发布的文件，有关部门和各地应高度重视部分行业产能过剩和重复建设问题，正确把握抑制产能过剩和重复建设的政策导向，坚决抑制产能过剩和重复建设。";
        //"高技区北环海路128号";

        System.out.println( getSimScore(str,str2)) ;
    }

    public static double getSimScore(String doc1,String doc2)
    {
        Character[] list = LCS.getStr(doc1);
        Character[] list2 = LCS.getStr(doc2);

        List<Character> list3 = longestCommonSubsequence(list, list2);

        int lcs = list3.size();
        return( lcs / (double)Math.min(doc1.length(), doc2.length())) ;
    }

    public static Character[] getStr(String str) {
        Character[] list = new Character[str.length()];
        try {
            int begin = 0;
            int end = str.length();

            while (begin < end) {
                char c = str.charAt(begin);
                list[begin] = c;
                begin++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <E> List<E> longestCommonSubsequence(E[] s1, E[] s2) {
        int[][] num = new int[s1.length + 1][s2.length + 1]; // 2D array,

        // Actual algorithm
        for (int i = 1; i <= s1.length; i++)
            for (int j = 1; j <= s2.length; j++)
                if (s1[i - 1].equals(s2[j - 1]))
                    num[i][j] = 1 + num[i - 1][j - 1];
                else
                    num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);

        //System.out.println("length of LCS = " + num[s1.length][s2.length]);

        int s1position = s1.length, s2position = s2.length;
        List<E> result = new LinkedList<E>();

        while (s1position != 0 && s2position != 0) {
            if (s1[s1position - 1].equals(s2[s2position - 1])) {
                result.add(s1[s1position - 1]);
                s1position--;
                s2position--;
            } else if (num[s1position][s2position - 1] >= num[s1position - 1][s2position]) {
                s2position--;
            } else {
                s1position--;
            }
        }
        Collections.reverse(result);
        return result;
    }

}
