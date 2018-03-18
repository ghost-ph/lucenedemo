package com.patterncat.lucene.pageRank;

import java.io.Serializable;
import java.util.*;

/**
 * A simple directed graph in which multiple edges between
 * any two vertices are <i>not</i> permitted, and vertices are
 * <i>not</i> permitted to link to themselves.
 *
 * @author Dave Perrett <dev@recurser.com>
 */
public class DirectedGraph<T> {

    /**
     * Map for storing vertices.
     */
    private Map<T,EdgeContainer<T>> vertexMap;

    /**
     * Set for storing edges.
     */
    private Set<Edge> edgeSet;

    /**
     * Constructor
     */
    public DirectedGraph() {
        this.vertexMap = new LinkedHashMap<T,EdgeContainer<T>> ();
        this.edgeSet   = new LinkedHashSet<Edge> ();
    }

    /**
     * Returns the set of vertices in this graph.
     */
    public Set<T> vertexSet() {
        return this.vertexMap.keySet();
    }

    /**
     * Returns the set of edges in this graph.
     *
     * @return <code>Set</code> of edges.
     */
    public Set<Edge> edgeSet() {
        return this.edgeSet;
    }

    /**
     * Adds the given collection of vertices to the
     * graph. Returns true if the graph has been
     * modified, false otherwise.
     *
     * @param vertices The collection of vertices to add.
     *
     * @return <code>true</code> if the graph is modified.
     */
    public boolean addAllVertices(Collection<T> vertices) {
        boolean modified = false;

        for (Iterator<T> iterator = vertices.iterator(); iterator.hasNext();) {
            modified |= this.addVertex(iterator.next());
        }

        return modified;
    }

    /**
     * Returns true if this graph has an edge between the
     * given start vertex and end vertex, false otherwise.
     *
     * @param startVertex Start vertex of the edge.
     * @param endVertex   End vertex of the edge.
     *
     * @return <code>true</code> if this graph contains the edge.
     */
    public boolean containsEdge(T startVertex, T endVertex) {
        return this.getEdge(startVertex, endVertex ) != null;
    }

    /**
     * Returns true if this graph contains the given edge,
     * false otherwise
     *
     * @param edge Edge to search for.
     *
     * @return <code>true</code> if this graph contains the edge.
     */
    public boolean containsEdge(Edge edge) {
        return this.edgeSet.contains(edge);
    }

    /**
     * Returns true if this graph contains the given vertex,
     * false otherwise.
     *
     * @param vertex Vertex to search for.
     *
     * @return <code>true</code> if this graph contains the vertex.
     */
    public boolean containsVertex(T vertex) {
        return this.vertexMap.containsKey(vertex);
    }

    /**
     * Adds a new edge connecting the given vertices.
     *
     * @param startVertex Start vertex of the edge.
     * @param endVertex   End vertex of the edge.
     *
     * @return New <code>Edge</code>.
     */
    public Edge addEdge(T startVertex, T endVertex) {

        // Make sure both the vertices exist
        this.assertVertexExists(startVertex);
        this.assertVertexExists(endVertex);

        // Return null if this edge is already in the graph
        if (this.containsEdge(startVertex, endVertex)) {
            return null;

            // Ignore pages that link to themselves
        } else if (startVertex.equals(endVertex)) {
            return null;
        }

        // Add the new edge to the graph
        Edge<T> edge = new Edge<T> (startVertex, endVertex);
        this.edgeSet.add(edge);
        this.addEdgeToTouchingVertices(edge);
        return edge;
    }

    /**
     * Adds the given edge to the graph.
     *
     * @param edge New edge to add.
     *
     * @return <code>true</code> if the operation is a success.
     *
     * @throws NullPointerException if specified edge is <code>null</code>.
     */
    public boolean addEdge(Edge<T> edge) {

        if (edge == null ) {
            throw new NullPointerException();

        } else if (this.containsEdge(edge)) {
            return false;
        }

        // Get the vertices for this edge
        T startVertex = edge.getStartVertex();
        T endVertex   = edge.getEndVertex();

        // Make sure both the vertices exist
        this.assertVertexExists(startVertex);
        this.assertVertexExists(endVertex);

        // Return false if this edge is already in the graph
        if (this.containsEdge(startVertex, endVertex)) {
            return false;
        }

        // Add the edge to the graph
        this.edgeSet.add(edge);
        addEdgeToTouchingVertices(edge);
        return true;
    }

    /**
     * Ensures that the given vertex exists in this graph,
     * and throws exception if not found.
     *
     * @param vertex Vertex to check.
     *
     * @return <code>true</code> if this assertion holds.
     *
     * @throws NullPointerException if specified vertex is <code>null</code>.
     * @throws IllegalArgumentException if specified vertex does not exist in
     *         this graph.
     */
    protected boolean assertVertexExists(T vertex) {

        if (this.containsVertex(vertex)) {
            return true;

        } else if (vertex == null) {
            throw new NullPointerException();

        } else {
            throw new IllegalArgumentException("No such vertex in the graph");
        }
    }

    /**
     * Adds the given vertex to this graph.
     *
     * @param vertex Vertex to add.
     *
     * @return <code>true</code> if the operation is a success.
     *
     * @throws NullPointerException if given vertex is <code>null</code>.
     */
    public boolean addVertex(T vertex) {

        if (vertex == null ) {
            throw new NullPointerException();

        } else if (this.containsVertex(vertex)) {
            return false;

        } else {
            // add with a lazy edge container entry
            this.vertexMap.put(vertex, null);
            return true;
        }
    }

    /**
     * Returns the edge from the graph that connects the given
     * vertices. Returns null if the edge doesn't exist.
     *
     * @param startVertex Start vertex of the edge.
     * @param endVertex   End vertex of the edge.
     *
     * @return <code>Edge</code> that connects the vertices.
     */
    public Edge getEdge(T startVertex, T endVertex) {

        if (this.containsVertex(startVertex) && this.containsVertex(endVertex)) {

            Iterator iterator = this.getEdgeContainer(startVertex).getOutgoingEdges().iterator();
            while (iterator.hasNext()) {
                Edge edge = (Edge) iterator.next();
                if (edge.getEndVertex().equals(endVertex)) {
                    return edge;
                }
            }
        }
        return null;
    }

    /**
     * Return an array of vertices linking to this vertex
     *
     * @return <code>ArrayList</code> of vertices linking to this vertex
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> getIncomingVertices(T vertex) {
        ArrayList<T> result = new ArrayList<T> ();

        // Get the set of vertices linking to this vertex
        Iterator<Edge> edgeIterator = this.incomingEdgesOf(vertex).iterator();
        while (edgeIterator.hasNext()) {
            Edge edge = edgeIterator.next();
            result.add((T) edge.getStartVertex());
        }

        return result;
    }

    /**
     * Return an array of vertices linked to by this vertex
     *
     * @return <code>ArrayList</code> of vertex linked to by this vertex
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> getOutgoingVertices(T vertex) {
        ArrayList<T> result = new ArrayList<T> ();

        // Get the set of vertices linked to by this vertex
        Iterator edgeIterator = this.outgoingEdgesOf(vertex).iterator();
        while (edgeIterator.hasNext()) {
            Edge edge = (Edge) edgeIterator.next();
            result.add((T) edge.getEndVertex());
        }

        return result;
    }

    /**
     * Adds the given edge as an outgoing edge to it's
     * start and end vertices.
     *
     * @param edge Edge to process.
     */
    public void addEdgeToTouchingVertices(Edge<T> edge) {
        T startVertex = edge.getStartVertex();
        T endVertex = edge.getEndVertex();

        this.getEdgeContainer(startVertex).addOutgoingEdge(edge);
        this.getEdgeContainer(endVertex).addIncomingEdge(edge);
    }

    /**
     * Returns the number of incoming edges pointing
     * to the given vertex.
     *
     * @param vertex Vertex to check.
     *
     * @return Number of edges pointing to the vertex.
     */
    public int inDegreeOf(T vertex) {
        return this.getEdgeContainer(vertex).getIncomingEdges().size();
    }

    /**
     * Returns the list of incoming edges pointing
     * to the given vertex.
     *
     * @param vertex Vertex to check.
     *
     * @return <code>List</code> of edges pointing to the vertex.
     */
    public List incomingEdgesOf(T vertex) {
        return this.getEdgeContainer(vertex).getIncomingEdges();
    }

    /**
     * Returns the number of outgoing edges pointing
     * away from the given vertex.
     *
     * @param vertex Vertex to check.
     *
     * @return Number of edges pointing away from the vertex.
     */
    public int outDegreeOf(T vertex) {
        return this.getEdgeContainer(vertex).getOutgoingEdges().size();
    }

    /**
     * Returns the list of outgoing edges pointing
     * away from the given vertex.
     *
     * @param vertex Vertex to check.
     *
     * @return <code>List</code> of edges pointing away from the vertex.
     */
    public List outgoingEdgesOf(T vertex) {
        return this.getEdgeContainer(vertex).getOutgoingEdges();
    }


    /**
     * Returns a lazy-built edge container for the given vertex.
     *
     * @param vertex A vertex in this graph.
     *
     * @return <code>EdgeContainer</code>
     */
    private EdgeContainer<T> getEdgeContainer(T vertex) {

        // Make sure the vertex exists
        this.assertVertexExists(vertex);

        // Try to get an existing container
        EdgeContainer<T> container = this.vertexMap.get(vertex);

        // Create a new container if none is found
        if (container == null ) {
            container = new EdgeContainer<T> (vertex);
            this.vertexMap.put(vertex, container);
        }

        return container;
    }


    /**
     * A container of for vertex edges.
     */
    private static class EdgeContainer<T> implements Serializable {

        /**
         * List of incoming edges.
         */
        List<Edge> incoming;

        /**
         * List of outgoing edges.
         */
        List<Edge> outgoing;

        /**
         * Constructor.
         */
        EdgeContainer(T vertex) {
            this.incoming = new ArrayList<Edge> (1);
            this.outgoing = new ArrayList<Edge> (1);
        }

        /**
         * Returns the list of incoming edges for this container.
         *
         * @return <code>List</code>
         */
        public List getIncomingEdges(  ) {
            return this.incoming;
        }

        /**
         * Returns the list of outgoing edges for this container.
         *
         * @return <code>List</code>
         */
        public List getOutgoingEdges(  ) {
            return this.outgoing;
        }

        /**
         * Adds an incoming edge to this container.
         *
         * @param edge <code>Edge</code> to add.
         */
        public void addIncomingEdge(Edge edge) {
            this.incoming.add(edge);
        }

        /**
         * Adds an outgoing edge to this container.
         *
         * @param edge <code>Edge</code> to add.
         */
        public void addOutgoingEdge( Edge e ) {
            this.outgoing.add( e );
        }
    }
}
