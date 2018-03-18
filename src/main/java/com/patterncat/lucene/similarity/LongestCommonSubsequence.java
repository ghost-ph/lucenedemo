package com.patterncat.lucene.similarity;

public class LongestCommonSubsequence {

    public static void main(String[] args) {
        String str = //"基认为，就塔霍人的所有要求在2014年找到解决方案令人期待，但不能为实现这个目标付出过高代价。 ";
                "高新技术开发区北环海路128号";
        String str2 = //"根据发展改革委、工业和信息化部、监察部、财政部、国土资源部、环境保护部、人民银行、质检总局、银监会、证监会发布的文件，有关部门和各地应高度重视部分行业产能过剩和重复建设问题，正确把握抑制产能过剩和重复建设的政策导向，坚决抑制产能过剩和重复建设。";
                "高技区北环海路128号";

        System.out.println( longestCommonSubsequence(str,str2)) ;
    }

    public static String longestCommonSubsequence(String s1, String s2) {
        int[][] num = new int[s1.length() + 1][s2.length() + 1]; // 2D array,

        // Actual algorithm
        for (int i = 1; i <= s1.length(); i++)
            for (int j = 1; j <= s2.length(); j++)
                if (s1.charAt(i - 1)==(s2.charAt(j - 1)))
                    num[i][j] = 1 + num[i - 1][j - 1];
                else
                    num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);

        //System.out.println("length of LCS = " + num[s1.length][s2.length]);

        int s1position = s1.length(), s2position = s2.length();
        StringBuilder result = new StringBuilder();

        while (s1position != 0 && s2position != 0) {
            if (s1.charAt(s1position - 1)==s2.charAt(s2position - 1)) {
                result.append(s1.charAt(s1position - 1));
                s1position--;
                s2position--;
            } else if (num[s1position][s2position - 1] >= num[s1position - 1][s2position]) {
                s2position--;
            } else {
                s1position--;
            }
        }
        result.reverse();
        return result.toString();
    }
}

