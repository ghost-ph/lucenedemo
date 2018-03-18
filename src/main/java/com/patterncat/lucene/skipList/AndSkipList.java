package com.patterncat.lucene.skipList;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 * @author Administer
 * @see
 * @2013-7-30
 */
public class AndSkipList {
    private static void addDoc(IndexWriter writer, String content)
            throws IOException {
        FieldType fieldType = new FieldType();
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setIndexed(true);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(true);
        Document doc = new Document();
        doc.add(new Field("content", content, fieldType));
        writer.addDocument(doc);
    }

    public static void main(String[] args) throws IOException, ParseException
    {
        test();
    }


    public static void testDoc() throws IOException, ParseException {

        Directory directory = new RAMDirectory();
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_43);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        addDoc(writer, "bla bla bla bleu bleu");
        addDoc(writer, "bla bla bla bla");
        writer.close();
        DirectoryReader reader = DirectoryReader.open(directory);
        DocsEnum de = MultiFields.getTermDocsEnum(reader, MultiFields
                .getLiveDocs(reader), "content", new BytesRef("bla"));
        int doc;
        while ((doc = de.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
            System.out.println(de.docID() +" "+ de.freq());
        }

    }

    public static void test() throws IOException, ParseException{
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_43);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        addDoc(writer, "bla bla bla bleu bleu");
        addDoc(writer, "bleu bla bla bla bla");
        writer.close();
        DirectoryReader reader = DirectoryReader.open(directory);
        DocsEnum de = MultiFields.getTermDocsEnum(reader, MultiFields
                .getLiveDocs(reader), "content", new BytesRef("bla"));
        int doc;
        DocsEnum de2 = MultiFields.getTermDocsEnum(reader, MultiFields
                .getLiveDocs(reader), "content", new BytesRef("bleu"));

        DocsEnumAnd(de,de2);
        reader.close();
    }

    //输出t1和t2两个文档序列都有的文档编号和词频
    public static void DocsEnumAnd(DocsEnum t1, DocsEnum t2) throws IOException {
        t1.nextDoc();
        t2.nextDoc();
        while (t1.docID() != DocsEnum.NO_MORE_DOCS
                && t2.docID() != DocsEnum.NO_MORE_DOCS) {
            if (t1.docID() < t2.docID()) {
                if (t1.advance(t2.docID()) == DocsEnum.NO_MORE_DOCS)
                    return;
            }
            if (t2.docID() < t1.docID()) {
                if (t2.advance(t1.docID()) == DocsEnum.NO_MORE_DOCS)
                    return;
            }
            if (t1.docID() == t2.docID()) {
                System.out.println("bla  found doc:" + t1.docID() +" freq:"+t1.freq());
                System.out.println("bleu  found doc:" + t2.docID() +" freq:"+t2.freq());
                t1.nextDoc();
            }
        }
    }

}

