package com.patterncat.lucene.spider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从指定网址抓取，抓取包含指定关键词的网页。
 *
 * @author luogang
 *
 */
public class MyCrawler {
    ArrayList<String> todo = new ArrayList<String>();
    HashSet<String> visited = new HashSet<String>();
    final String downPath = "d:/crawler/";
    String interestWord = "军事";//抓取指定关键词的网页

    public MyCrawler() {
        super();
        new File(downPath).mkdirs();
    }

    private String downloadPageContent(String strUrl) {
        try {
            // 根据网址strUrl创建URL对象
            URL pageUrl = new URL(strUrl);
            // System.out.println(pageUrl.getFile());
            // 下载网页
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    pageUrl.openStream()));
            String line;
            // 读取网页内容
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                pageBuffer.append(line);
            }
            return pageBuffer.toString();
        } catch (Exception e) {
        }
        return null;
    }

    private HashSet<String> retrieveLinks(String pageContents, URL pageUrl) {

        HashSet<String> hSLink = new HashSet<String>();
        if (!"".equals(pageContents) && pageContents != null) {
            // 提取所有超链接的URL
            Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
                    Pattern.CASE_INSENSITIVE);
            System.out.println(pageContents);
            Matcher m = p.matcher(pageContents);
            while (m.find()) {
                String strLink = m.group(1).trim();
                if (strLink.length() < 1) {
                    continue;
                }

                // 跳过链接，只是网页锚,忽略锚
                if (strLink.charAt(0) == '#') {
                    continue;
                }

                // 跳过mailto链接,忽略mailto
                if (strLink.indexOf("mailto:") != -1) {
                    continue;
                }

                // 忽略 JavaScript 链接.
                if (strLink.toLowerCase().indexOf("javascript") != -1) {
                    continue;
                }

                if (strLink.indexOf("://") == -1) {
                    // 处理绝对URL
                    if (strLink.charAt(0) == '/') {
                        strLink = "http://" + pageUrl.getHost() + strLink;
                        // 处理相对URL
                    } else {
                        String file = pageUrl.getFile();
                        if (file.indexOf('/') == -1) {
                            strLink = "http://" + pageUrl.getHost() + "/"
                                    + strLink;
                        } else {
                            String path = file.substring(0, file
                                    .lastIndexOf('/') + 1);
                            strLink = "http://" + pageUrl.getHost() + path
                                    + strLink;
                        }
                    }
                }
                int index = strLink.indexOf('#');
                if (index != -1) {
                    strLink = strLink.substring(0, index);
                }
                if (!visited.contains(strLink)) {
                    // 如果页面包含指定关键词就抓取
                    if (pageContents.indexOf(interestWord) != -1) {
                        // addToDoURL(Link);
                        hSLink.add(strLink);
                    }
                }
            }
        }
        return hSLink;
    }

    public void run() {

        while (todo.size() > 0) {
            System.out.println("queueURL大小是" + todo.size());
            printArray();
            // 从Todo队列里面提取，是否有URL
            String strUrl = todo.iterator().next();
            // 提取URL的内容
            String Content = downloadPageContent(strUrl);
            try {
                // 提取URL链接存储到Todo队列里面
                HashSet<String> newLinks = retrieveLinks(Content, new URL(strUrl));
                todo.addAll(newLinks);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // 从Todo队列里删除已经爬过的URL
            todo.remove(strUrl);
            // 从Todo队列里删除的URL,添加到Visited集合中
            visited.add(strUrl);
        }
    }

    public void printArray() {
        FileWriter visiter;
        BufferedWriter writer = null;
        try {
            visiter = new FileWriter(downPath + "viciter"
                    + System.currentTimeMillis() + ".txt");
            writer = new BufferedWriter(visiter);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Iterator<String> to = todo.iterator();
        while (to.hasNext()) {
            String url = to.next();
            // System.out.println(queueURL.size()+"Todo队列的ＵＲＬ＝"+url);

            File file = new File("Todo队列" + System.currentTimeMillis() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                FileWriter out = new FileWriter(file);
                out.write(todo.size() + "Todo队列的ＵＲＬ＝" + url);
                out.write("\t");
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        Iterator<String> visitor = visited.iterator();
        while (visitor.hasNext()) {
            String url = (String) visitor.next();
            try {
                writer.write(url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            File file = new File("访问表" + System.currentTimeMillis() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                FileWriter out = new FileWriter(file);
                out.write(visited.size() + "访问表的ＵＲＬ＝" + url);
                out.write("\t");
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println(visitedURLList.size()+"访问表的ＵＲＬ＝"+url);
        }
        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        // 创建自己的网络蜘蛛
        MyCrawler crawler = new MyCrawler();
        // 加入入口地址
        crawler.todo.add("http://www.sina.com");
        // 运行
        crawler.run();
    }
}
