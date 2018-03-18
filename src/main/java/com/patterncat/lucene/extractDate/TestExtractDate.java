package com.patterncat.lucene.extractDate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestExtractDate {

    public static void main(String[] args) throws Exception {
        String inputStr = "今天是2009-12-6";
        Pattern p = Pattern.compile("\\d{2,4}-\\d{1,2}-\\d{1,2}");
        Matcher m = p.matcher(inputStr);
        if(m.find()){
            String strDate = m.group();
            System.out.println(strDate);
        }
    }
}
