package com.patterncat.lucene.synonym;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.Version;

public class TestSynonymCoord {

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

        Field f1 = new TextField("content", "quick fast", Store.YES);
        doc1.add(f1);
        writer.addDocument(doc1);
        Document doc2 = new Document();
        Field f2 = new TextField("content", "quick hops", Store.YES);
        doc2.add(f2);
        writer.addDocument(doc2);
        writer.close();
        IndexReader reader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(reader);

        Similarity similarity = new MySimilarity();
        searcher.setSimilarity(similarity);

        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        builder.add(new CharsRef("fast"), new CharsRef("quick"), true);
        builder.add(new CharsRef("jumps"), new CharsRef("hops"), true);
        SynonymMap map = builder.build();

        analyzer = new SynonymAnalyzer(map);
        QueryParser parser = new QueryParser(Version.LUCENE_44, "content",
                analyzer);
        Query query = parser.parse("fast jumps");
        System.out.println(query.toString("content"));
        System.out.println(query.getClass().getName());
        TopDocs docs = searcher.search(query, 2);
        for (ScoreDoc doc : docs.scoreDocs) {
            System.out.println("----------");
            System.out.print("docid : " + doc.doc + " score : " + doc.score + " content:");
            Document hitDoc = searcher.doc(doc.doc);
            System.out.println(hitDoc.get("content")); //输出文档
            Explanation explanation = searcher.explain(query, doc.doc);
            System.out.println("explain: "+explanation.toString());
        }
    }

}

