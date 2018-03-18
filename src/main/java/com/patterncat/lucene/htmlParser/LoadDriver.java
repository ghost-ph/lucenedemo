package com.patterncat.lucene.htmlParser;

public class LoadDriver {
    public static void main(String[] args) {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            System.out.println("找到驱动程序");
        } catch (ClassNotFoundException ex) {
            System.out.println("没有找到驱动程序");
        }
    }
}
