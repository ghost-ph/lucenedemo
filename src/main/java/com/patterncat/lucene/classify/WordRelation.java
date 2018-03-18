package com.patterncat.lucene.classify;

public class WordRelation {
    private String word;        //关键词
    private String category;	//关联分类
    private int degree;			//关联度
    private int index;			//索引

    public WordRelation(String word,String category,int degree,int index){
        this.word= word;
        this.category = category;
        this.degree = degree;
        this.index = index;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getDegree() {
        return degree;
    }
    public void setDegree(int degree) {
        this.degree = degree;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}

