package com.patterncat.lucene.pageRank;

import java.util.*;

/**
 * Class representing a page in the page-rank-space.
 *
 * @author Dave Perrett <dev@recurser.com>
 */
public class RankablePage extends Page {

    /**
     * page-rank of this page.
     */
    private double pageRank = 0.0;

    /**
     * Directed graph for mapping pages with inter-connecting links.
     */
    protected DirectedGraph<RankablePage> graph;

    /**
     * Constructor
     *
     * @param urlString String representing the url for this page
     * @param graph Directed graph mapping page connections
     */
    public RankablePage(String urlString, DirectedGraph<RankablePage> graph) {
        super(urlString);
        this.graph = graph;
        this.graph.addVertex(this);
    }

    /**
     * Return the page rank for this page
     *
     * @return <code>double</code> page rank for this page
     */
    public double getPageRank() {
        return this.pageRank;
    }

    /**
     * Recalculate the page rank for this page, using the
     * decayFactor provided.
     *
     * @param decayFactor Simulates random jumps during graph traversal
     */
    public void recalculatePageRank(double decayFactor) {

        // Get the 'incoming' portion of the page rank :
        // The sum of each incoming page's pageRank divided by it's
        // number of outgoing links
        double incomingPortion = 0.0;
        Iterator iterator = this.graph.getIncomingVertices(this).iterator();
        while (iterator.hasNext()) {
            RankablePage page = (RankablePage) iterator.next();
            incomingPortion += (page.getPageRank() / this.graph.outDegreeOf(page));
        }

        this.pageRank = (1.0 - decayFactor) + (decayFactor * incomingPortion);
    }
}

