package com.patterncat.lucene.bigramSeg;

/**
 *  邻接矩阵表示的切分词图
 * @author luogang
 * @2010-3-18
 */
import java.util.Iterator;

public class AdjList implements  Iterable<CnToken> {
    private CnTokenLinkedList list[];// AdjList的图的结构
    public int verticesNum;

    /**
     * Constructor: Allocates right amount of space
     */
    public AdjList(int verticesNum) {
        this.verticesNum = verticesNum;
        list = new CnTokenLinkedList[verticesNum];

        // initialize all of the linked lists in the array
        for (int index = 0; index < verticesNum; index++) {
            list[index] = new CnTokenLinkedList();
        }
    }

    /**
     * add an edge to the graph
     */
    public void addEdge(CnToken newEdge) {
        list[newEdge.end].put(newEdge);
    }

    /**
     * Returns an iterator that contains the Edges leading to adjacent nodes
     */
    public Iterator<CnToken> getPrev(int vertex) {
        CnTokenLinkedList ll = list[vertex];
        if(ll == null)
            return null;
        return ll.iterator();
    }

    /**
     * Feel free to make this prettier, if you'd like
     */
    public String toString() {
        StringBuilder temp = new StringBuilder();

        for (int index = 0; index < verticesNum; index++) {
            if(list[index] == null)
            {
                continue;
            }
            temp.append("node:");
            temp.append(index);
            temp.append(": ");
            temp.append(list[index].toString());

            temp.append("\n");
        }

        return temp.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<CnToken> iterator() {
        // TODO Auto-generated method stub
        return new AdjIterator();
    }

    private class AdjIterator implements Iterator<CnToken> {
        int pos;
        Iterator<CnToken> it ;
        public AdjIterator()
        {
            pos = 1;  //去掉开始节点
            it = list[pos].iterator();
        }

        public boolean hasNext() {
            if(it.hasNext())
                return true;
            while(pos < (verticesNum-1)){
                pos++;
                it = list[pos].iterator();
                if(it.hasNext())
                    return true;
            }
            return false;
        }

        public CnToken next() {
            if(!hasNext())
            {
                return null;
            }
            CnToken t = it.next();
            return t;
        }

        public void remove() {
            //itr.remove();
        }
    }

    public CnTokenLinkedList prevWordList(int start) {
        if(start<0)
            return null;
        return list[start];
    }
}
