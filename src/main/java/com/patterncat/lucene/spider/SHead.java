package com.patterncat.lucene.spider;

import java.net.*;
import java.io.*;

/**
 * 通过socket的HTTP GET，不通过"URL"类下载
 * @author luogang
 *
 */
public class SHead {
    public static void main(String[] args) throws IOException {
        String frontURL = "http://www.lietu.com/index.jsp";
        //"http://www.tendata.cn/";
        //"http://www.mysteel.com/";
        URL u = new URL(frontURL);

        Socket s = null;

        try {
            String host = u.getHost();// 主机名 这里是 www.lietu.com
            System.out.println(host);
            String file = u.getPath();// 网页路径 这里是 /index.jsp
            System.out.println(file);
            int port = 80; // 端口号

            s = new Socket(host, port);

            OutputStream out = s.getOutputStream();
            PrintWriter outw = new PrintWriter(out, false);
            outw.print("HEAD " + file + " HTTP/1.0\r\n");
            //outw.print("User-Agent: Internet Explorer\r\n");
            //User-Agent", "Internet Explorer
            //outw.print("Accept: text/plain, text/html, text/*\r\n");
            outw.print("\r\n");
            outw.flush();

            InputStream in = s.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }

        if (s != null) {
            try {
                s.close();
            } catch (IOException ioEx) {
            }
        }
    }
}

