package com.patterncat.lucene.pageRank;

/**
 * Simple abstract class representing a web page.
 *
 * @author Dave Perrett <dev@recurser.com>
 */
public abstract class Page {

    /**
     * String representation of this page's url.
     */
    protected String urlString;

    /**
     * Constructor
     */
    public Page(String urlString) {
        this.urlString = urlString;
    }

    /**
     * Return a string representation of this page
     *
     * @return <code>String</code> representation of this page
     */
    public String getUrl() {
        return this.urlString;
    }

}

