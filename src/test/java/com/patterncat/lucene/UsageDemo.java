package com.patterncat.lucene;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * http://www.norconex.com/upgrading-code-to-lucene-4/
 * http://www.lucenetutorial.com/index.html
 * https://github.com/macluq/helloLucene
 * Created by patterncat on 2016-02-07.
 */
public class UsageDemo {

    File indexDir = new File(this.getClass().getClassLoader().getResource("").getFile());

    @Test
    public void createIndex() throws IOException {
//        Directory index = new RAMDirectory();
        Directory index = FSDirectory.open(indexDir);
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);

        // 1. create the index
        IndexWriter w = new IndexWriter(index, config);
        addDoc(w, "Lucene in Action", "193398817");
        addDoc(w, "Lucene for Dummies", "55320055Z");
        addDoc(w, "Managing Gigabytes", "55063554A");
        addDoc(w, "The Art of Computer Science", "9900333X");
        w.close();
    }

    private void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }

    @Test
    public void iterateIndex() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir));
        List<AtomicReaderContext> leaves = reader.leaves();
        for (AtomicReaderContext context : leaves) {
            AtomicReader atomicReader = context.reader();
            Fields fields = atomicReader.fields();
            for (String fieldName : fields) {
                System.out.println("field:"+fieldName);
                Terms terms = atomicReader.terms(fieldName);
                TermsEnum te = terms.iterator(null);
                BytesRef term;
                while ((term = te.next()) != null) {
                    System.out.println(term.utf8ToString());
                }
            }
        }
    }

    @Test
    public void search() throws IOException {
        // 2. query
        String querystr = "lucene";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = null;
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
            q = new QueryParser(Version.LUCENE_46,"title", analyzer).parse(querystr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. search
        int hitsPerPage = 10;
        Directory index = FSDirectory.open(indexDir);
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    /**
     * https://github.com/wgybzbrobot/sina-services/blob/d7b2b995d067c6b641a2b92f3dbb5b811a8ec433/fuzzy-search/src/main/java/cc/pp/fuzzy/search/analyzer/MMSeg4jAnalyzerDemo.java
     * @throws IOException
     */
    @Test
    public void cutWords() throws IOException {
//        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
//        CJKAnalyzer analyzer = new CJKAnalyzer(Version.LUCENE_46);
        SimpleAnalyzer analyzer = new SimpleAnalyzer();
        String text = "Spark是当前最流行的开源大数据内存计算框架，采用Scala语言实现，由UC伯克利大学AMPLab实验室开发并于2010年开源。";
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                System.out.println(charTermAttribute.toString());
            }
            tokenStream.end();
        } finally {
            tokenStream.close();
            analyzer.close();
        }
    }
}
