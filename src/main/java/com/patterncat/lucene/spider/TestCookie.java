package com.patterncat.lucene.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;

public class TestCookie {
    public static void main(String[] args) throws IOException {
        String content=loginGoogle("ООО  РУБИНС ИМПОРТ");
//		String url=loginGoogle("ООО  ДЕЛИС");

        // 返回网页内容
        System.out.println(content);
    }

    public static String loginGoogle(String name) throws SocketException{
        try {
            String Searchword =URLEncoder.encode(name,"Windows-1251");

            String url="http://www.google.com.hk/search?hl=uk&safe=strict&q="+Searchword+".&btnG=%D0%9F%D0%BE%D1%88%D1%83%D0%BA&aq=f&aqi=&aql=&oq=&gs_rfai=";


            URL pageURL = new URL(url);
            System.out.println(pageURL);

            HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();

            urlConnection.setRequestProperty("User-Agent","MSIE 6.0");

            urlConnection.setRequestProperty("Host", pageURL.getHost());
            urlConnection.setRequestProperty("Accept-Language", "ru");
            urlConnection.setRequestProperty("Accept-Charset","gb2312,utf-8;q=0.7,*;q=0.7n");
            urlConnection.setRequestProperty("content-type", "text/html");
            // Read from the connection. Default is true.
            urlConnection.setDoOutput(true);
            // Read from the connection. Default is true.
            urlConnection.setDoInput(true);

            java.io.InputStream urlStream = urlConnection.getInputStream();
            // 创建一个使用默认大小输入缓冲区的缓冲字符输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream,"Windows-1251"));
            String line;
            // 读取网页内容
            StringBuilder pageBuffer = new StringBuilder();// 线程安全的可变字符序列
            while ((line = reader.readLine()) != null) {
//					System.out.println(line);
                pageBuffer.append(line);
            }
            urlStream.close();//网络流
            return pageBuffer.toString();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (ConnectException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

