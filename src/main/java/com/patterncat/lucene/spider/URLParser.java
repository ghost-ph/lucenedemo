package com.patterncat.lucene.spider;

import java.net.URI;
import java.net.URL;

/**
 * 打印url地址的主机名、路径。
 * @author luogang
 *
 */
public class URLParser {

    public static void main(String args[]) throws Exception {
        URL url = new URL("http://www.webmonkey.com.cn/html/html40/a.html");
        System.out.println("Authority=" + url.getAuthority());
        System.out.println("Host=" + url.getHost());
        System.out.println("Path=" + url.getPath());
        System.out.println("port=" + url.getPort());
        System.out.println("Query=" + url.getQuery());


        URI uri = new URI("http://www.webmonkey.com.cn/html/html40/a.html");
        System.out.println("Authority=" + uri.getAuthority());
        System.out.println("Host=" + uri.getHost());
        System.out.println("Path=" + uri.getPath());
        System.out.println("Scheme=" + uri.getScheme());
        System.out.println("Query=" + uri.getQuery());
    }
}

