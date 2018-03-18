package com.patterncat.lucene.basic;

public class News {
    public String URL;
    public String title;
    public StringBuffer body;

    public News(){
        this.URL = "";
        this.title = "";
        this.body = new StringBuffer();
    }

    public String toString(){
        return "URL :"+URL+" title :" + title +" body :"+ body.toString() ;
    }
}
