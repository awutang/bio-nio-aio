/**
 * Author: Tang Yuqian
 * Date: 2021/2/14
 */
package com.io.learning.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();//监听新进来的 TCP连接的通道，打开 ServerSocketChannel

        ssc.socket().bind(new InetSocketAddress(8080));//绑定8080端口

        ssc.configureBlocking(false);//设置非阻塞模式

        Selector selector = Selector.open();//创建选择器

        SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);//给选择器注册通道

        //selectionKey：代表了注册到该 Selector 的通道

        while (true) { //监听新进来的连接

            int select = selector.select(2000);
            if (select==0) { //如果选择的通道为0，最长会阻塞 timeout毫秒

                System.out.println("等待请求超时......");

                continue;
            }

            System.out.println("开始处理请求.....");

            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();//迭代器

            while (keyIter.hasNext()) {

                SelectionKey key = keyIter.next();

                new Thread(new HttpHandler(key)).run();

                keyIter.remove();

            }
        }
    }
}
