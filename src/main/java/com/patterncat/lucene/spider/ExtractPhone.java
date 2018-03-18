package com.patterncat.lucene.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractPhone {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String example = "电话号码:81727660.";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(example);
        // 检查所有的出现
        while (matcher.find()) {
            System.out.println("提取的电话号码: ");
            System.out.println(example.substring(matcher.start(), matcher.end()));
        }
    }

}

