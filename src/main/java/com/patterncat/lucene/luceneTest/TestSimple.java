package com.patterncat.lucene.luceneTest;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TestSimple {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_44,
                analyzer);

        System.out.println(iwc.getCodec().availableCodecs());

        iwc.setCodec(new SimpleTextCodec());
        iwc.setUseCompoundFile(false);

        // setup Lucene to use an in-memory index
        // Directory directory = new RAMDirectory();
        Directory directory = FSDirectory.open(new File("F:/lucene/index"));

        IndexWriter writer = new IndexWriter(directory, iwc);

        // index a few documents
        writer.addDocument(createDocument("1", "青菜鸡肉"));
        writer.addDocument(createDocument("2", "老鸭粉丝汤"));
        writer.addDocument(createDocument("3", "辣子鸡丁"));
        writer.close();
    }

    private static Document createDocument(String id, String content) {
        Document doc = new Document();
        doc.add(new Field("id", id, StringField.TYPE_STORED));
        doc.add(new Field("contents", content, TextField.TYPE_STORED));
        return doc;
    }
}
