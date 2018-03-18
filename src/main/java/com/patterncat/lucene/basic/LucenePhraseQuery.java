package com.patterncat.lucene.basic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LucenePhraseQuery {
    public static void main(String[] args) throws Exception {
        // setup Lucene to use an in-memory index
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);

        // index a few documents
        writer.addDocument(createDocument("1", "foo bar baz"));
        writer.addDocument(createDocument("2", "red green blue"));
        writer.addDocument(createDocument("3", "test foo bar test"));
        writer.close();

        // search for documents that have "foo bar" in them
        String sentence = "foo bar";
        IndexReader reader = IndexReader.open(directory);
        // 根据IndexReader创建IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);
        PhraseQuery query = new PhraseQuery();
        String[] words = sentence.split(" ");
        for (String word : words) {
            query.add(new Term("contents", word));
        }

        // display search results
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
    }

    private static Document createDocument(String id, String content) {
        Document doc = new Document();
        doc.add(new Field("id", id, Store.YES, Index.NOT_ANALYZED));
        doc.add(new Field("contents", content, Store.YES, Index.ANALYZED,
                Field.TermVector.WITH_POSITIONS_OFFSETS));
        return doc;
    }
}
