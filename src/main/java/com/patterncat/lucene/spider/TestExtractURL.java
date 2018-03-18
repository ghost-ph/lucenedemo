package com.patterncat.lucene.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestExtractURL {

    public static void main(String[] args) throws Exception {

        String pageContents = "<a href='http://www.lietu.com'>猎兔</a>";
        String email = "123com";
        System.out.println(email.matches("\\d+.*"));
        Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(pageContents);
        while (m.find()) {//提取网页中的链接
            String link = m.group(1).trim();
            System.out.println(link);
        }
    }
}

