package com.patterncat.lucene.spider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 多个SocketChannel注册Selector。
 *
 *
 */
public class SocketChannelSelector {
    public static SocketChannel createSocketChannel(String hostName, int port)
            throws IOException {
        SocketChannel sChannel = SocketChannel.open();
        sChannel.configureBlocking(false);
        sChannel.connect(new InetSocketAddress(hostName, port));
        return sChannel;
    }

    // 2个连接注册的选择器关键字
    static SelectionKey key1;
    static SelectionKey key2;

    public static void main(String[] args) {
        // 1个选择器，注册2个Socket 通道
        Selector selector = null;
        try {
            // 创建选择器
            selector = Selector.open();

            // 创建2个通道
            SocketChannel sChannel1 = createSocketChannel("hao123.com", 80);
            SocketChannel sChannel2 = createSocketChannel("www.lietu.com", 80);

            // 注册选择器，侦听所有的事件
            key1 = sChannel1.register(selector, sChannel1.validOps());
            key2 = sChannel2.register(selector, sChannel1.validOps());
        } catch (IOException e) {
        }

        // 等待事件的循环
        while (true) {
            try {
                // 等待
                selector.select();
            } catch (IOException e) {
                break;
            }

            // 所有事件列表
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            // 处理每一个事件
            while (it.hasNext()) {
                // 得到关键字
                SelectionKey selKey = it.next();

                // 删除已经处理的关键字
                it.remove();

                try {
                    // 处理事件
                    processSelectionKey(selKey);
                } catch (IOException e) {
                    // 处理异常
                    selKey.cancel();
                }
            }
        }
    }

    public static void processSelectionKey(SelectionKey selKey)
            throws IOException {
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        // 确认连接正常
        if (selKey.isValid() && selKey.isConnectable()) {
            // 得到通道
            SocketChannel sChannel = (SocketChannel) selKey.channel();

            // 是否连接完毕？
            boolean success = sChannel.finishConnect();
            if (!success) {
                // 异常
                selKey.cancel();
            }
        }

        // 如果可以读取数据
        if (selKey.isValid() && selKey.isReadable()) {
            // 得到通道
            SocketChannel sChannel = (SocketChannel) selKey.channel();

            if (sChannel.read(buf) > 0) {
                // 转到最开始
                buf.flip();

                while (buf.remaining() > 0) {
                    System.out.print((char) buf.get());
                }

                // 复位，清空
                buf.clear();
            }
        }

        // 如果可以写入数据
        if (selKey.isValid() && selKey.isWritable()) {
            // 得到通道
            SocketChannel sChannel = (SocketChannel) selKey.channel();
            // 区分2个侦听器的关键字
            // 我这里只写一次数据。
            if (!s1 && key1.equals(selKey)) {
                System.out.println("channel1 write data..");
                //buf.clear();
                //buf.put("HELO localhost\n".getBytes());
                //buf.flip();
                //sChannel.write(buf);
                sendQuery(new URL("www.lietu.com"),sChannel);
                s1 = true;
            } else if (!s2 && key2.equals(selKey)) {
                System.out.println("channel2 write data..");
				/*buf.clear();
				buf.put("HELO localhost\n".getBytes());
				buf.flip();
				sChannel.write(buf);*/
                sendQuery(new URL("hao123.com"),sChannel);
                s2 = true;
            }
        }
    }

    /**
     *@return true if the query was succesfully send, false otherwise
     * (send buffer too small or problems sending the query).
     */
    // ------------------------------------------------------------
    protected static boolean sendQuery ( URL url, SocketChannel sc_ )
    {
        StringBuilder qb = new StringBuilder() ;
        qb.append("GET ") ;

        String path = url.getPath() ;
        if( path == null || path.length() == 0 ) path = "/" ;
        qb.append( path.replaceAll(" ","%20") ) ;
        if( url.getQuery() != null )
        {
            qb.append( '?' ) ;
            //qb.append( URLEncoder.encode( url.getQuery() )) ;
            qb.append(url.getQuery().replaceAll(" ","%20")) ;
        }
        qb.append( " HTTP/1.0\r\n" ) ;
        qb.append( "Host: " ).append( url.getHost() ).append( "\r\n" ) ;

        byte[] bytes_array = qb.toString().getBytes() ;
        int _DEFAULT_SEND_BUFFER_SIZE = 8192 * 2 ;
        ByteBuffer _send_buff = ByteBuffer.allocate( _DEFAULT_SEND_BUFFER_SIZE ) ;
        //_logger.trace( "\n--- sending ---\n" + qb.toString() + "\n------") ;
        _send_buff.clear() ;
        _send_buff.put( bytes_array ) ;
        _send_buff.flip() ;

        try
        {
            sc_.write( _send_buff ) ;
        }
        catch (IOException e)
        {
            //if( _logger.isTraceEnabled() )
            //    _logger.trace("IOException when trying to send request to URL[" + query_.getURL() + "]");
            return false;
        }
        //write_total_time += new Date().getTime() - t ;

        return true ;
    }

    // 判断已经写过数据的标志
    static boolean s1 = false;
    static boolean s2 = false;
}
