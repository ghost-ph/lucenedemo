package com.patterncat.lucene.index;

import java.util.*;

/**
 * DatabaseStatistics represents statistics on a collection of Documents. It
 * contains the statistics for each individual Document as well as statistics on
 * the entire collection.
 */
public class DatabaseConsumer {
    /** The collection of DocumentStatistics, indexed by DOCID. */
    HashMap<Integer,DocConsumer> documents;

    /** The current DocumentStatistics to register words with */
    DocConsumer currentDocument;

    /** The database length, in words */
    int words;

    /** Initialize all members. */
    public DatabaseConsumer() {
        words = 0;
        documents = new HashMap<Integer, DocConsumer>();
    }

    /** set the current document */
    public void setCurrentDocument(String docid) {
        currentDocument = documents.get(docid);
    }

    /**
     * getDocumentStatistics
     *
     * Gets the specific statistics for the document specified by the docid.
     *
     * @param docid
     *            the unique identifier of the Document
     * @returns the object with the specific statistics for the specified
     *          document
     */
    public DocConsumer getDocumentStatistics(int docid) {
        return documents.get(docid);
    }

    /** Get a list of DOCID in random order. */
    public Iterator<Integer> getDocumentList() {
        return documents.keySet().iterator();
    }
}
