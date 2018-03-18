package com.patterncat.lucene.scorer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class TestCoord {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        RAMDirectory directory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        Document doc1 = new Document();

        Field f1 = new TextField("content", "common hello world", Store.YES);
        doc1.add(f1);
        writer.addDocument(doc1);
        Document doc2 = new Document();
        Field f2 = new TextField("content", "common common common", Store.YES);
        doc2.add(f2);
        writer.addDocument(doc2);
        writer.close();
        IndexReader reader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser(Version.LUCENE_44, "content",
                new StandardAnalyzer(Version.LUCENE_44));
        Query query = parser.parse("common world");
        TopDocs docs = searcher.search(query, 2);
        for (ScoreDoc doc : docs.scoreDocs) {
            System.out.println("docid : " + doc.doc + " score : " + doc.score);
        }
    }

}

