package com.patterncat.lucene.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 自动重起modem的程序，用来更换ip地址
 * @author luogang
 *
 */
public class ChangeIp {
    public static void main(String[] args) throws IOException {
        Socket s = null;

        try {
            //modem的用户名和密码
            String data = "admin:admin";
            String authorization = Base64.encode(data.getBytes());
            System.out.println(authorization);

            String host = "192.168.1.1"; //modem的ip地址
            String file = "/userRpm/SysRebootRpm.htm?Reboot=%D6%D8%C6%F4%C2%B7%D3%C9%C6%F7"; // 网页路径
            int port = 80; // 端口号

            s = new Socket(host, port);

            OutputStream out = s.getOutputStream();
            PrintWriter outw = new PrintWriter(out, false);
            outw.print("GET " + file + " HTTP/1.1\r\n");
            outw.print("Authorization:Basic "+authorization+"\r\n");
            outw.print("\r\n");
            outw.flush();

            InputStream in = s.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            // br.close(); // Q. Do I need this?
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

    public static boolean restartRouter(String file,String host,int port,String userpassword)
    {
        boolean result=false;
        Socket s = null;
        try {
            //String host = "192.168.1.101"; // 主机名

            //String file = "/userRpm/SysRebootRpm.htm?Reboot=%D6%D8%C6%F4%C2%B7%D3%C9%C6%F7"; // 网页路径
            //int port = 80; // 端口号

            s = new Socket(host, port);

            OutputStream out = s.getOutputStream();
            PrintWriter outw = new PrintWriter(out, false);
            outw.print("GET " + file + " HTTP/1.1\r\n");//dGhoOmhodGlhbg==
            outw.print("Authorization:Basic "+userpassword+"\r\n");
            outw.print("\r\n");
            outw.flush();

            InputStream in = s.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inr);
            String line;
            line = br.readLine();
            br.close();
            if (line!=null&&(!line.contains("403")))
                result=true;
        }
        catch	(UnknownHostException e)
        {
            return false;
        }
        catch (IOException e) {
            return false;
        }
        finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ioEx) {
                    return true;
                }
            }
        }
        return result;
    }
}

