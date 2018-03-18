package com.patterncat.lucene.spider;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * 异步下载网页
 *
 */
public class SyDown {

    public static Selector sel = null;
    public static Map<SocketChannel, String> sc2Path = new HashMap<SocketChannel, String>();

    public static void setConnect(String ip, String path, int port) {
        try {
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress(ip, port));
            client.register(sel, SelectionKey.OP_CONNECT | SelectionKey.OP_READ
                    | SelectionKey.OP_WRITE); //
            sc2Path.put(client, path);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        try {
            sel = Selector.open();
            setConnect("www.lietu.com", "/index.jsp", 80); // 添加一个下载网址
            setConnect("hao123.com", "/book.htm", 80);
            System.out.println("start...");

            while (!sel.keys().isEmpty()) {
                if (sel.select(100) > 0) {
                    Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        try {
                            processSelectionKey(key);
                        } catch (IOException e) {
                            key.cancel();
                            e.printStackTrace(System.err);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void processSelectionKey(SelectionKey selKey)
            throws IOException {
        SocketChannel sChannel = (SocketChannel) selKey.channel();
        if (selKey.isValid() && selKey.isConnectable()) {
            boolean success = sChannel.finishConnect();
            if (!success) {
                selKey.cancel();
            }
            sendMessage(sChannel, "GET " + sc2Path.get(sChannel)
                    + " HTTP/1.0\r\nAccept: */*\r\n\r\n");
        } else if (selKey.isReadable()) {
            String ret = readMessage(sChannel);
            if (ret != null && ret.length() > 0) {
                System.out.println(ret);
                // selKey.cancel();
            } else {
                // System.err.println("cancel");
                selKey.cancel();
            }
        }
    }

    // 下载网页
    public static String readMessage(SocketChannel client) {
        String result = null;
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            int i = client.read(buf);
            buf.flip();
            if (i != -1) {
                result = new String(buf.array(), 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 发送 http请求
    public static boolean sendMessage(SocketChannel client, String msg) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf = ByteBuffer.wrap(msg.getBytes());
            client.write(buf);
        } catch (IOException e) {
            return true;
        }
        return false;
    }

}

