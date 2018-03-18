package com.patterncat.lucene.htmlParser;

import java.io.IOException;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserException;

public class TestIFRAME {

    /**
     * @param args
     * @throws IOException
     * @throws ParserException
     */
    public static void main(String[] args) throws ParserException, IOException {
        String path = "http://tianya.cn";
        URL urlPage = new URL(path);
        Lexer lexer = new Lexer(urlPage.openConnection()); // 解析网页
        Node node;
        while (null != (node = lexer.nextNode())) {
            if (!(node instanceof TagNode)) {// 判断node是否TagNode类型的节点
                continue;
            }

            TagNode tagNode = (TagNode) node; // 强制转换类型
            String name = tagNode.getTagName();// 根据名字判断是否IFRAME
            if (name.equals("IFRAME") && !tagNode.isEndTag()) {
                String newURL = tagNode.getAttribute("src");
                URL u = new URL(new URL(path), newURL);// 取得URL的绝对地址
                System.out.println(u.toString());
            }
        }

    }

}
