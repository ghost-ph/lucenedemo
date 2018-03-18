package com.patterncat.lucene.basic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Administer
 * @see
 * @2013-7-23
 */
public class TestWildcardquery {

    private static final String FIELD = "contents";

    public static void main(String[] args) throws Exception {
        // setup Lucene to use an in-memory index
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);

        // index a few documents
        writer.addDocument(createDocument("1", "foo bar baz"));
        writer.addDocument(createDocument("2", "red green blue"));
        writer.addDocument(createDocument("3",
                "The Lucene was made by Doug Cutting"));
        writer.close();


        IndexReader reader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(reader);

        Query query = new WildcardQuery(new Term(FIELD, "cut*"));
        // 自动在结尾添加 *
        // display search results
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
    }

    private static Document createDocument(String id, String content) {
        Document doc = new Document();
        doc.add(new Field("id", id, StringField.TYPE_STORED));
        doc.add(new Field("contents", content,  TextField.TYPE_STORED));
        return doc;
    }

}
