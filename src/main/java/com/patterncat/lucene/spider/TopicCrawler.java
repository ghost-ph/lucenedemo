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
 * 主题爬虫
 * @author wangzhengdong,luogang
 *
 */
public class TopicCrawler {
    //待扩展的URL队列
    ArrayList toExtendsURL = new ArrayList();
    //存放主题关键词的数组
    private static String[] keywords = new String[]{};
    //存放主题关键词的权重
    private static double[] w = new double[]{};
    //阀值
    private static final double e=0.01;
    //不相关的URL队列
    ArrayList notRelativeURL = new ArrayList();
    //已经访问的URL队列
    HashSet visiterURLList = new HashSet();
    //下载目录
    final String downPath = "c:/chuxiong/crawler/";
    //添加URL
    private void addExtendsURL(String url) {
        //添加将要扩展的URL
        notRelativeURL.add(url);
    }
    //
    //添加URL
    private void addNotRelativeURL(String url) {
        //添加将要扩展的URL
        toExtendsURL.add(url);
    }
    //构造函数
    public TopicCrawler() {
        super();
        new File(downPath).mkdirs();
    }
    //从扩展表中提取URL，下载网页内容，并且把从网页内容中提取的URL放入到扩展队列中去（在提取出URL的时候要经过相关度计算）
    private void deleterExtendsURL() {
        //从扩张表里面提取看，是否有URL
        String url = (String) toExtendsURL.iterator().next();
        //提取URL
        String Content = downloadPageContent(url);
        try {
            // 提取URL链接存储到扩张表里面
            retrieveLinks(Content, new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //从扩张表里删除已经爬去过的URL
        toExtendsURL.remove(url);
        // 添加，从扩张表里删除的URL
        visiterURLList.add(url);
    }
    //下载网页，提取其中的链接
    private String downloadPageContent(String str) {
        try {
            //根据STR创建URL对象
            URL pageUrl = new URL(str);
            // System.out.println(pageUrl.getFile());
            //下载网页
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    pageUrl.openStream()));
            String line;
            //读取网页内容
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                pageBuffer.append(line);
            }
            return pageBuffer.toString();
        } catch (Exception e) {
        }
        return null;
    }
    //从下载的网页内容上面提取URL
    private void retrieveLinks(String pageContents, URL pageUrl) {

        if (!"".equals(pageContents) && pageContents != null) {
            // 提取有超链接的URL
            Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
                    Pattern.CASE_INSENSITIVE);
            System.out.println(pageContents);
            Matcher m = p.matcher(pageContents);
            while (m.find()) {
                String link = m.group(1).trim();
                if (link.length() < 1) {
                    continue;
                }

                // Skip links that are just page anchors.忽略
                if (link.charAt(0) == '#') {
                    continue;
                }

                // Skip mailto links.忽略mailto
                if (link.indexOf("mailto:") != -1) {
                    continue;
                }

                // 忽略 JavaScript links.
                if (link.toLowerCase().indexOf("javascript") != -1) {
                    continue;
                }

                if (link.indexOf("://") == -1) {
                    // Handle absolute URLs.处理绝对URL
                    if (link.charAt(0) == '/') {
                        link = "http://" + pageUrl.getHost() + link;
                        // Handle relative URLs.处理相对URL
                    } else {
                        String file = pageUrl.getFile();
                        if (file.indexOf('/') == -1) {
                            link = "http://" + pageUrl.getHost() + "/" + link;
                        } else {
                            String path = file.substring(0, file
                                    .lastIndexOf('/') + 1);
                            link = "http://" + pageUrl.getHost() + path + link;
                        }
                    }
                }
                int index = link.indexOf('#');
                if (index != -1) {
                    link = link.substring(0, index);
                }
                if (!visiterURLList.contains(link)) {
                    // 如果页面包含军事就抓
                    if (pageContents.indexOf("军事") != -1) {
                        addExtendsURL(link);
                    }

                }
                //对链接进行相关度分析，如果小于预定义的阀值，就必须过滤掉
                if(!isRelative(pageContents)){
                    addNotRelativeURL(link);
                }
            }
        }

    }
    //计算获取的页面的相关性
    private boolean isRelative(String pageContents){
        int[] x = new int[keywords.length];
        String tempString = pageContents;
        int k=-1;
        for(int i=0;i<keywords.length;i++){
            while((k=tempString.indexOf(keywords[i]))!=-1){
                x[i]++;
                tempString = tempString.substring(k+keywords[i].length());
            }
        }
        double sum=0,sum1=0,sum2=0;
        for(int i=0;i<x.length;i++){
            sum+=x[i]*Math.pow(w[i], 2);
            sum1+=Math.pow(x[i], 2)*Math.pow(w[i], 2);
            sum2+=Math.pow(w[i], 2);
        }
        double cos = sum/(Math.sqrt(sum1)*Math.sqrt(sum2));
        if(cos<e){
            return false;
        }
        return true;
    }
    public void run() {

        while (toExtendsURL.size() > 0) {
			/*
			 * if(toExtendsURL.size()>10000){ return; }
			 */
            System.out.println("toExtendsURL大小" + toExtendsURL.size());
            printArray();
            deleterExtendsURL();
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
        Iterator to = toExtendsURL.iterator();
        while (to.hasNext()) {
            String url = (String) to.next();
            // System.out.println(toExtendsURL.size()+"扩张表的ＵＲＬ＝"+url);

            File file = new File("扩张" + System.currentTimeMillis() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                FileWriter out = new FileWriter(file);
                out.write(toExtendsURL.size() + "扩张表的ＵＲＬ＝" + url);
                out.write("\t");
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        Iterator visitor = visiterURLList.iterator();
        while (visitor.hasNext()) {
            String url = (String) visitor.next();
            try {
                writer.write(url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            File file = new File("访问" + System.currentTimeMillis() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                FileWriter out = new FileWriter(file);
                out.write(visiterURLList.size() + "访问表的ＵＲＬ＝" + url);
                out.write("\t");
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println(visiterURLList.size()+"访问表的ＵＲＬ＝"+url);
        }
        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

