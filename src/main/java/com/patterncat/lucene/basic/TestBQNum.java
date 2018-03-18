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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Administer
 * @see
 * @2013-7-23
 */
public class TestBQNum {

    private static final String FIELD = "contents";

    public static void main(String[] args) throws Exception {
        // setup Lucene to use an in-memory index
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);

        // index a few documents
        writer.addDocument(createDocument("1", "青菜鸡肉"));
        writer.addDocument(createDocument("2", "老鸭粉丝汤"));
        writer.addDocument(createDocument("3",
                "辣子鸡丁"));
        writer.close();

        IndexReader reader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(reader);

        Term t1 = new Term(FIELD, "鸡");
        TermQuery q1 = new TermQuery(t1);

        Term t2 = new Term(FIELD, "鸭");
        TermQuery q2 = new TermQuery(t2);

        BooleanQuery mbq = new BooleanQuery();
        mbq.add(q1 , BooleanClause.Occur.SHOULD);
        mbq.add(q2 , BooleanClause.Occur.SHOULD);
        mbq.setMinimumNumberShouldMatch(1);

        // display search results
        TopDocs topDocs = searcher.search(mbq, 10);
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
