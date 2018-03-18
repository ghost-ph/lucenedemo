package com.patterncat.lucene.pageRank;

import java.util.Hashtable;

/**
 * Factory for creating and keeping track of rankable pages. When
 * queried for a page url, it will search the hash for that url, and
 * return an existing RankablePage if possible. If no existing page is
 * found, it will create a new CrawlableRemotePage object and store
 * it in the hash.
 *
 * @author Dave Perrett <dev@recurser.com>
 */
public class RankablePageFactory {

    /**
     * Maps urls to CrawlableRemotePage objects
     */
    private static Hashtable<String,RankablePage> pages;

    /**
     * Constructor
     */
    private RankablePageFactory() {}

    /**
     * Return a page for the given url if it is already present in the
     * hash. If it isn't present already, create a new page, store it
     * in the hash, and return it.
     *
     * @param urlString url for the page
     * @param graph Graph mapping pages and links
     *
     * @return page matching the given url
     */
    public static RankablePage getPage(String urlString, DirectedGraph<RankablePage> graph) {

        if (pages == null) {
            pages = new Hashtable<String,RankablePage> ();
        }

        // Try and get an existing page from the hash
        RankablePage page = (RankablePage) pages.get(urlString);

        if (page == null) {
            page = new RankablePage(urlString, graph);
            pages.put(urlString, page);
        }

        return page;
    }

    /**
     * Return true if this link is already in the hash
     * table, false otherwise
     *
     * @param urlString url whose presence to check
     */
    public static boolean isKnownPage(String urlString) {
        return pages.containsKey(urlString);
    }

}
