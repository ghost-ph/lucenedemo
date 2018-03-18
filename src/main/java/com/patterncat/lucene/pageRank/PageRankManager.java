package com.patterncat.lucene.pageRank;

import java.util.*;

/**
 * Takes a list of link-pairs as a constructor argument,
 * and iteratively calculates the page-ranks of the
 * given pages.
 *
 * @author Dave Perrett <dev@recurser.com>
 */
public class PageRankManager {

    /**
     * Directed graph for mapping pages with inter-connecting links.
     */
    private DirectedGraph<RankablePage> graph = new DirectedGraph<RankablePage> ();

    /**
     * Constructor
     *
     * @param links List of link-pairs to rank.
     */
    public PageRankManager(ArrayList<String[]> links) {

        // Build our page graph from the link-pair list
        this.generateNodeGraph(links);
    }

    /**
     * Run in 'run' mode : Calculate the page rank of the pages in
     * the graph using the given numIterations and decayFactor.
     *
     * @param numIterations Number of iterations to run to refine the rank.
     * @param decayFactor Decay factor to introduce to simulate random page-jumping.
     * @param precision Number of decimal places to round the pageRank to.
     */
    public void run(int numIterations, double decayFactor, int precision) {

        System.out.println("Running " + numIterations + " iterations...");

        // Iteratively re-calculate the pageRanks of the pages
        for (int i = 1; i <= numIterations; i++) {

            // Calculate the page ranks
            Iterator pageIterator = this.graph.vertexSet().iterator();
            double totalPageRank = 0.0;
            while (pageIterator.hasNext()) {
                RankablePage page = (RankablePage) pageIterator.next();
                if (graph.outDegreeOf(page) > 0) {
                    page.recalculatePageRank(decayFactor);
                }
                totalPageRank += page.getPageRank();
            }

            // Output some runtime information
            if (i % 10 == 0 || i == numIterations) {
                double averagePageRank = totalPageRank / this.graph.vertexSet().size();
                System.out.println("Iteration " + i + " : (average rank " +
                        this.roundDouble(averagePageRank, precision) + ")");
            }
        }

        System.out.println("");
        System.out.println("-----------------------");
        System.out.println("        RESULT");
        System.out.println("-----------------------");

        // Output the final ranks
        Iterator pageIterator = this.graph.vertexSet().iterator();
        double totalPageRank = 0.0;
        while (pageIterator.hasNext()) {
            RankablePage page = (RankablePage) pageIterator.next();
            totalPageRank += page.getPageRank();
            System.out.println(page.getUrl() + " (" +
                    this.roundDouble(page.getPageRank(), precision) + ")");
        }

        double averagePageRank = totalPageRank / this.graph.vertexSet().size();
        System.out.println("");
        System.out.println("-----------------------");
        System.out.println("Average PageRank : " + this.roundDouble(averagePageRank, precision));
        System.out.println("-----------------------");
    }

    /**
     * Populate the graph of pages, storing incoming and outgoing pages for each.
     *
     * @param fileName Path to the file containing the link-list.
     */
    private void generateNodeGraph(ArrayList<String[]> links) {

        Iterator linkIterator = links.iterator();
        int lineNum = 1;
        while (linkIterator.hasNext()) {
            String[] linkPair = (String[]) linkIterator.next();

            // Output an error if the line is mal-formed
            if (linkPair.length != 2) {
                System.err.println("Warning : Line " + lineNum + " contains " +
                        linkPair.length + " links [skipping]");
                continue;
            }

            // Get RankablePage objects for each of these links, and add
            // them to the graph, along with an edge between them
            RankablePage fromPage = RankablePageFactory.getPage(linkPair[0], this.graph);
            RankablePage toPage = RankablePageFactory.getPage(linkPair[1], this.graph);
            this.graph.addEdge(fromPage, toPage);

            // Increment the line count
            lineNum++;
        }
    }

    /**
     * Round the specified double to the specified precision.
     *
     * @param num Number to round.
     * @param precision Number of decimal places to round to.
     */
    private double roundDouble(double num, int precision) {
        double vector = 1.0;
        while (precision-- > 0) {
            vector *= 10.0;
        }
        return Math.round(num * vector) / vector;
    }

}
