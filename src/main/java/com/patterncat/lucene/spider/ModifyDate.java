package com.patterncat.lucene.spider;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ModifyDate {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String frontURL = //"http://www.lietu.com/";
                //"http://www.tendata.cn/";
                "http://www.steelccl.com/";
        URL u = new URL(frontURL);
        HttpURLConnection http = (HttpURLConnection) u.openConnection();
        http.setRequestMethod("HEAD");
        //http.setRequestProperty("User-Agent", "Internet Explorer");
        //http.setRequestProperty("Http-version", "HTTP/1.0");
        //http.setRequestProperty("Accept",
        //"text/plain, text/html, text/*");

        //Accept: text/plain, text/html, text/*\r\n
        System.out.println(u + " 更新时间 :" + http.getLastModified());
        long modifyDateLong = http.getLastModified();
        if(modifyDateLong ==0)
        {
            modifyDateLong = http.getHeaderFieldDate("Last-Modified", 0);
            System.out.println(u + " 更新时间 " + modifyDateLong);
        }

        System.out.println(u + " 长度 :" + http.getContentLength());
        Map<String,List<String>> header = http.getHeaderFields();
        System.out.println(http.getClass().toString());

        for (Entry<String,List<String>> e:header.entrySet()) {
            System.out.println(e.getKey()+e.getValue());
        }

    }
}

