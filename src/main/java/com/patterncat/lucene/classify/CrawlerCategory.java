package com.patterncat.lucene.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CrawlerCategory {

    private static String CATEGORY="category";
    private static String FILE_CATEGORY_PATH;
    private static String FILE_CATEGORY_XML_PATH ;

    private Set<String> CATEGORY_SET = null;
    private static DocumentBuilderFactory dbfactory =DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;
    private static Document document;

    static {
        InputStream inputFile = null;
        Properties propertie = new Properties();
        try {
            inputFile = CrawlerCategory.class.getClassLoader()
                    .getResourceAsStream("conf.properties");
            propertie.load(inputFile);
            FILE_CATEGORY_PATH =propertie.getProperty("categorys");
            FILE_CATEGORY_XML_PATH=propertie.getProperty("categoryxml");
        } catch (FileNotFoundException ex) {
            System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("装载文件--->失败!");
            ex.printStackTrace();
        } finally {
            try {
                if (inputFile != null)
                    inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * 读取文本文档
     */
    public void readerCategory(Document doc) throws IOException {
        BufferedReader br = null;
        File f = new File(FILE_CATEGORY_PATH);
        if (!f.exists()) {
            return ;
        }
        newInstanceSet();
        br = new BufferedReader(new FileReader(f));
        String line = "";
        addNode(br,line,doc);
        if (br != null) {
            br.close();
        }
    }

    public void newInstanceSet() {
        if (null == CATEGORY_SET) {
            CATEGORY_SET = new HashSet<String>();
        }
    }
    //读取xml
    public Set<String> readerDocument(){
        File f =new File(FILE_CATEGORY_XML_PATH);
        try {
            if(!f.exists()){
                createDocument();
            }

            DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
            DocumentBuilder db =dbf.newDocumentBuilder();
            Document doc = db.parse(f);
            newInstanceSet();
            NodeList nodeList = doc.getChildNodes();

            for(int i=0;i<nodeList.getLength();i++){
                getNode(nodeList.item(i).getChildNodes());
            }

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return CATEGORY_SET;
    }

    private void getNode(NodeList nodeList){
        for(int i=0;i<nodeList.getLength();i++)
        {
            CATEGORY_SET.add(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue());
        }
    }

    //写入映射到xml文件
    public void domParser(Document doc) throws ParserConfigurationException,
            IOException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer trans = factory.newTransformer();

        DOMSource source = new DOMSource(doc);

        File file = new File(FILE_CATEGORY_XML_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(file);
        StreamResult result = new StreamResult(out);
        trans.transform(source, result);

    }
    //创建document
    public void createDocument() throws ParserConfigurationException {
        builder = dbfactory.newDocumentBuilder();
        try {
            document = builder.newDocument();
            document.setXmlVersion("1.0");

            readerCategory(document);

            domParser(document);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //添加节点
    private void addNode(BufferedReader br,String line ,Document doc) throws DOMException, IOException{
        Element root =doc.createElement("categories");
        Element node = null;
        while((line=br.readLine())!=null){
            if(line.indexOf("<1>")!=-1){
                line = subLine(line);
                node = doc.createElement(CATEGORY);
                node.setAttribute("name", line);
                root.appendChild(node);
            }else if(line.indexOf("<2>")!=-1){
                addChildNode(br,line,doc,node);
            }
        }
        doc.appendChild(root);
    }
    private void addChildNode(BufferedReader br,String line,Document doc,Element root) throws IOException{
        Element childNode;
        while((line=br.readLine())!=null){
            if(line.indexOf("</2>")!=-1){
                return ;
            }
            childNode = doc.createElement(CATEGORY);
            childNode.setTextContent(line.trim());
            root.appendChild(childNode);
        }

    }
    private String subLine(String line){
        line = line.replaceAll("<1>", "");
        line = line.replaceAll("</1>", "");
        return line.trim();
    }
}
