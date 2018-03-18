package com.patterncat.lucene.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SocketHead {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String frontURL = // "http://www.lietu.com/index.jsp";
                "http://www.tendata.cn/";
        //"http://www.tendata.cn/";
        //"http://www.steelccl.com/";
        //"http://www.lietu.com/";
        //"http://www.hlgnet.com";
        //"http://diveintomark.org/xml/atom.xml";

        URL u = new URL(frontURL);

        Socket s = null;

        try {
            // http://www.steelccl.com/A/info_jghz.asp
            String host = u.getHost();// //"www.steelccl.com"; // 主机名
            String file = u.getPath();// "/A/info_jghz.asp"; // 网页路径
            int port = 80; // 端口号

            s = new Socket(host, port);

            OutputStream out = s.getOutputStream();
            PrintWriter outw = new PrintWriter(out, false);
            outw.print("GET " + file + " HTTP/1.0\r\n");
            //outw.print("Accept-encoding: gzip\r\n");
            //outw.print("Range: bytes=0-500\r\n");
            outw.print("If-None-Match: \"1272af65f918cb1:84f\"\r\n");

            //outw.print("If-Modified-Since: Thu, 01 Jul 2001 07:24:54 GMT\r\n");
            // User-Agent", "Internet Explorer
            //outw.print("Accept: text/plain, text/html, text/*\r\n");
            //outw.print("Accept: gzip\r\n");
            outw.print("\r\n");
            outw.flush();

            InputStream in = s.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inr);

            Map<String, String> header = new HashMap<String, String>();

            String line;
			/*line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);
			line = br.readLine();
			System.out.println(line);*/
			/*while ((line = br.readLine()) != null) {
					int pos = line.indexOf(":");
					if(pos==-1)break;
					String k = line.substring(0, pos );
					String v = line.substring(pos+1, line.length());
					//System.out.println(b);
					//System.out.println(c);
					header.put(k, v);
			}

			for (Entry<String, String> e:header.entrySet())
			{
				System.out.println(e.getKey()+":"+e.getValue());
			}*/

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

