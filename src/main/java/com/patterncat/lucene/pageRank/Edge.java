package com.patterncat.lucene.pageRank;

/**
 * An edge between two vertices in a simple directed graph.
 *
 * @author Dave Perrett <dev@recurser.com>
 */
public class Edge<T> {

    /**
     * Beginning vertex of this edge
     */
    private T startVertex;

    /**
     * End vertex of this edge
     */
    private T endVertex;

    /**
     * Constructor
     */
    public Edge(T startVertex, T endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    /**
     * Return the start vertex for this edge
     */
    public T getStartVertex() {
        return this.startVertex;
    }

    /**
     * Return the end vertex for this edge
     */
    public T getEndVertex() {
        return this.endVertex;
    }
}
