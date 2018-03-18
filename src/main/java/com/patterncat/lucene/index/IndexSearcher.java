package com.patterncat.lucene.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * IndexSearcher provides a means of searching a collection of Documents given
 * an InvertedIndex and a DocumentIndex. It is separated from Query for modular
 * searching.
 *
 * @see InvertedIndex
 * @see DocumentIndex
 */
public class IndexSearcher {
    InvertedIndex index; //倒排索引
    DocumentIndex docIndex; //文档索引

    /** create a new searcher */
    public IndexSearcher(InvertedIndex index, DocumentIndex docIndex) {
        this.index = index;
        this.docIndex = docIndex;
    }

    /** Search with no restriction on maximum number of document matches. */
    public ScoreDoc[] search(String[] termVector) {
        return search(termVector, Integer.MAX_VALUE);
    }

    /** 最多返回一定数量的搜索结果 */
    public ScoreDoc[] search(String[] termVector, int maxResults) {
        // this is where scores (weights)
        // are stored (docid -> double[1])
        Hashtable<String, float[]> scores = new Hashtable<String, float[]>();

        int collectionsize = docIndex.size();
        int avgdoclen = docIndex.avg_doclen();

        // loop through all term vectors and assign weights
        for (int i = 0; i < termVector.length; i++) {
            HashMap<String, int[]> docs; // the Hashtable linking docid to tf
            // get the Hashtable of all documents containing termVector[i] and
            // their respective term frequencies
            docs = index.documentsContaining(termVector[i]);

            // if the term is not in the database, skip it
            if (docs == null)
                continue;

            Iterator<String> docList; // the list of documents for the current
            // term

            // get an iterator for the documents containing this term
            docList = docs.keySet().iterator();

            // define the number of documents this term occurs in
            int df = docs.size();

            // loop through all documents containing this term
            while (docList.hasNext()) {

                String docid; // temporary variable for document id
                // get the next document id
                docid = docList.next();

                // load the term frequency
                int tf = ((int[]) docs.get(docid))[0];

                DocumentData data = docIndex.getDocumentData(docid);
                int max_tf = data.max_tf;
                int doclen = data.doclen;

                float[] wArray = (float[]) scores.get(docid);
                if (wArray == null) { // 以前没包含这个文档，所以把这个文档加入hashtable
                    wArray = new float[1];
                    wArray[0] = 0;
                    scores.put(docid, wArray);
                }

                // add the weight from this term into the existing sum of
                // weights
                wArray[0] += Scorer.weight(tf, max_tf, df, doclen, avgdoclen,
                        collectionsize);
            }
        }

        // second, we fill up the SDPair array to be sorted
        ScoreDoc[] results = new ScoreDoc[scores.size()];
        int i = 0;

        Iterator<String> docList = scores.keySet().iterator();
        while (docList.hasNext()) {
            String docid = docList.next();
            results[i++] = new ScoreDoc(docIndex.getDocumentData(docid),
                    ((float[]) scores.get(docid))[0]);
        }

        // third, we create some data structure that has them sorted by weight
        Arrays.sort(results);

        // lastly, handle the maxResults thing
        if (maxResults < results.length) {
            ScoreDoc[] newResults = new ScoreDoc[maxResults];
            for (i = 0; i < newResults.length; i++) {
                newResults[i] = results[i];
            }
            results = newResults;
        }

        return results;
    }
}
