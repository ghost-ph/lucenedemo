package com.patterncat.lucene.synonym;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.Version;

public class SynonymSearchTest {
    public static void main(String[] args) throws Exception {
        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        builder.add(new CharsRef("fast"), new CharsRef("quick"), true);
        builder.add(new CharsRef("jumps"), new CharsRef("hops"), true);
        SynonymMap map = builder.build();

        SynonymAnalyzer analyzer = new SynonymAnalyzer(map);

        RAMDirectory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        Document doc = new Document();
        doc.add(new TextField("content", "The quick brown fox jumps over the lazy dog", Store.YES));
        writer.addDocument(doc);
        writer.close();
        IndexReader reader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(reader);
        // 查询匹配"hops"的文档
        TermQuery termQuery = new TermQuery(new Term("content", "hops"));
        System.out.println(searcher.search(termQuery, 10).totalHits);
        // 查询匹配短语"fox hops"的文档
        PhraseQuery phraseQuery = new PhraseQuery();
        phraseQuery.add(new Term("content", "fox"));
        phraseQuery.add(new Term("content", "hops"));
        System.out.println(searcher.search(phraseQuery, 10).totalHits);

        reader.close();
    }
}
