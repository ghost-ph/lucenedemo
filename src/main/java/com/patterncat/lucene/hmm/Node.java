package com.patterncat.lucene.hmm;

public class Node {
    public Node bestPrev;
    public State tag;
    public double prob;
    public Node(State tag){
        this.tag=tag;
    }
    public String toString(){
        if(bestPrev!=null){
            return "tag:"+tag.name+" bestPrev:"+bestPrev.tag.name+" prob:"+prob;
        }
        return "tag:"+tag.name+" bestPrev:"+" prob:"+prob;
    }
}
