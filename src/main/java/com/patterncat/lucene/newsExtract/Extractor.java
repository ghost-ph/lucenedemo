package com.patterncat.lucene.newsExtract;

import java.util.ArrayList;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Extractor {
    public static void main(String[] argv) throws Exception {
        DOMParser parser = new DOMParser(); // 创建DOM解析器
        parser.parse("http://www.gongkong.com/Common/Details.aspx?c=1&m=7&l=8&Type=news&CompanyID=0-8F38-217A2BF31D51&Id=2011042111562900003"); // 解析网页
        NewsInfo n = extractAll(parser.getDocument());// 从根节点开始遍历
        System.out.println(n.toString());
    }

    public static String textExtractor(Node root) {
        // 若是文本节点的话，直接返回
        if (root.getNodeType() == Node.TEXT_NODE) {
            return root.getNodeValue().trim();
        }
        if (root.getNodeType() == Node.ELEMENT_NODE) {
            Element elmt = (Element) root;
            // 抛弃脚本
            if (elmt.getTagName().equals("STYLE")
                    || elmt.getTagName().equals("SCRIPT"))
                return "";

            NodeList children = elmt.getChildNodes();
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < children.getLength(); i++) {
                text.append(textExtractor(children.item(i)));
            }
            return text.toString();
        }
        // 对其它类型的节点，返回空值
        return "";
    }

    static NewsInfo extractAll(Node documentNode) {
        NewsInfo needInfo = new NewsInfo();
        String infoSource = ""; /* 信息来源 */
        String publishDate = ""; /* 发布时间 */
        String title = ""; /* 标题 */
        String text = ""; /* 正文 */
        String author = ""; /* 作者 */
        int start = 0; // 正文的开始位置
        int end = 0; // 正文的结束位置

        boolean findTextStartFlag = false; /* 初始化找到正文开头标志 */
        boolean findTextEndFlag = false; /* 初始化找到正文结尾标志 */
        boolean findInfoSource = false; /* 初始化找到信息来源标志 */
        boolean findPublishDate = false; /* 初始化找到发布日期标志 */
        boolean findTextAuthor = false; /* 初始化找到作者标志 */

		/* 获取标题 */
        title = getClearTitle(documentNode);

		/* 取得DOM对象内的所有文本 */
        String content = textExtractor(documentNode);

		/* 执行信息提取 采用规则方式提取小段文本 */
        ArrayList<DocToken> ret = DocTagger.tag(content);
        if(ret==null)
            return null;

        for (int j = 0; j < ret.size(); j++) {
            DocToken token = ret.get(j);

			/* 信息来源 */
            if (token.type.equals(DocType.InfoSource)
                    && (findInfoSource == false)) {
                infoSource = token.termText;
                findInfoSource = true; /* 找到信息来源标志 */
                continue;
            }

			/* 作者 */
            if (token.type.equals(DocType.Author) && (findTextAuthor == false)) {
                author = token.termText;
                findTextAuthor = true; /* 找到作者 */
                if (author.length() >= 4) {
                    author = "";
                    findTextAuthor = false;
                }
                continue;
            }

			/* 发布时间 */
            if (token.type.equals(DocType.PublishDate)
                    && (findPublishDate == false)) {
                publishDate = token.termText;
                findPublishDate = true; /* 找到发布日期标志 */
                continue;
            }

			/* 正文前缀 */
            if (token.type.equals(DocType.PrefixText)
                    && (findTextStartFlag == false)) {
                start = j;
                findTextStartFlag = true;
                continue;
            }

			/* 正文后缀 */
            if (token.type.equals(DocType.SuffixText)
                    && (findTextEndFlag == false)
                    && (findTextStartFlag == true)) {
                end = j;
                findTextEndFlag = true;
                continue;
            }
        }

        text = "";
        if ((findTextStartFlag == true) && (findTextEndFlag == true)) {
            for (int j = start + 1; j < end - 1; j++) {
                text += ret.get(j).termText;
            }
        }

        // 去掉头尾的一些不需要的字符
        title = Entities.HTML40.unescape(title).trim();

		/* 处理其中的转义字符 */
        text = Entities.HTML40.unescape(text.toString()).trim();
        needInfo.title = title;
        needInfo.text = text;
        needInfo.author = author;
        needInfo.infoSource = infoSource;
        needInfo.publishDate = publishDate;

        return needInfo;
    }

    private static void getText(StringBuffer sb, Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            sb.append(node.getNodeValue());// 取得结点值，即开始与结束标签之间的信息
        }
        NodeList children = node.getChildNodes();
        if (children != null) {
            int len = children.getLength();
            for (int i = 0; i < len; i++) {
                getText(sb, children.item(i));// 递归遍历DOM树
            }
        }
    }

    private static boolean getTitle(StringBuffer sb, Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if ("title".equalsIgnoreCase(node.getNodeName())) {
                getText(sb, node);
                return true;
            }
        }
        org.w3c.dom.NodeList children = node.getChildNodes();
        if (children != null) {
            int len = children.getLength();
            for (int i = 0; i < len; i++) {
                if (getTitle(sb, children.item(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /* 获取网页标题 */
    public static String getClearTitle(Node node) {
        int startPos = 0;
        int endPos = 0;
        String title = "";
        StringBuffer sb = new StringBuffer();
        getTitle(sb, node);

        ArrayList<DocToken> ret = DocTagger.tag(sb.toString().trim());

        boolean findTitleStartFlag = false;
        boolean findTitleEndFlag = false;
        for (int i = 0; i < ret.size(); ++i) {
			/* 标题 */
            if (ret.get(i).type.equals(DocType.PrefixTitle)
                    && (findTitleStartFlag == false)
                    && (findTitleEndFlag == false)) {
                findTitleStartFlag = true; /* 找到标题开头标志 */
                startPos = i;
                continue;
            } else if (((ret.get(i).type.equals(DocType.SuffixTitle)) || (ret
                    .get(i).type.equals(DocType.PublishDate)))
                    && (findTitleEndFlag == false)) {
                findTitleEndFlag = true; /* 找到标题结尾标志 */
                endPos = i;
                continue;
            }
        }

		/* 根据发现标题的标志进行处理 */
        if (findTitleStartFlag == true) {
            if (findTitleEndFlag == true) {
				/* 从title的start标志 拷贝至 end标志 */
                for (int j = startPos + 1; j < endPos; j++) {
                    if (!ret.get(j).type.equals(DocType.Start)
                            && !ret.get(j).type.equals(DocType.End)
                            && !ret.get(j).type.equals(DocType.Link)
                            && !ret.get(j).type.equals(DocType.PrefixTitle)
                            && !ret.get(j).type.equals(DocType.SuffixTitle)) {
                        title += ret.get(j).termText;
                    }
                }
            } else {
				/* 从title的start标志 拷贝至 结尾 */
                for (int j = startPos + 1; j < ret.size(); j++) {
                    if (!ret.get(j).type.equals(DocType.Start)
                            && !ret.get(j).type.equals(DocType.End)
                            && !ret.get(j).type.equals(DocType.Link)
                            && !ret.get(j).type.equals(DocType.PrefixTitle)
                            && !ret.get(j).type.equals(DocType.SuffixTitle)) {
                        title += ret.get(j).termText;
                    }
                }
            }
        } else {
            if (findTitleEndFlag == true) {
                for (int j = 0; j < endPos; j++) {
                    if (!ret.get(j).type.equals(DocType.Start)
                            && !ret.get(j).type.equals(DocType.End)
                            && !ret.get(j).type.equals(DocType.Link)
                            && !ret.get(j).type.equals(DocType.PrefixTitle)
                            && !ret.get(j).type.equals(DocType.SuffixTitle)) {
                        title += ret.get(j).termText;
                    }
                }
            } else {
                title = sb.toString().trim();
            }
        }

        return title;
    }
}
