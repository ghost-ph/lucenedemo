package com.patterncat.lucene.nlp;

import java.util.Arrays;

public class Translate {
    public static void main(String[] args) {
        System.out.println(translate("中山路一十三号"));
    }

    public static String translate(String value) {
        char[] digitals = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' }; // 定义匹配中文数字；没有'百'
        Arrays.sort(digitals);
        char[] num = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }; // 定义转换阿拉伯数字
        String reStr = ""; // 接收中文数字
        // long reNum = 0; //将中文数字转换成阿拉伯数字
        char[] valueArr = value.toCharArray(); // 将字符串转化成字符数组
        for (int i = 0; i < valueArr.length; i++) {
            for (int j = 0; j < digitals.length; j++) {
                if (valueArr[i] == digitals[j]) { // 如果含有中文数字，则替换成阿拉伯数字，并保存到reStr中
                    valueArr[i] = num[j];
                } else if ((valueArr[i] == '十') || (valueArr[i] == '百')) {
                    valueArr[i] = ' ';
                }
            }
        }
        for (int k = 0; k < valueArr.length; k++) {
            if (valueArr[k] == ' ')
                continue;
            reStr += valueArr[k];
        }
        // reNum = Long.parseLong(reStr);
        return reStr;
    }
}

