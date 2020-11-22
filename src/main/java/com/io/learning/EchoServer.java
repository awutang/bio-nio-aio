/**
 * Author: Tang Yuqian
 * Date: 2020/11/22
 */
package com.io.learning;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 服务端 netty采用tcp协议
 *
 * 创建 ServerBootstrap 实例来引导服务器并随后绑定
 * 创建并分配一个 NioEventLoopGroup 实例来处理事件的处理，如接受新的连接和读/写数据。
 * 指定本地 InetSocketAddress 给服务器绑定
 * 通过 EchoServerHandler 实例给每一个新的 Channel 初始化
 * 最后调用 ServerBootstrap.bind() 绑定服务器
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);        //1 设置端口
        new EchoServer(port).start();                //2
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(); //3
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)                                //4
                    .channel(NioServerSocketChannel.class)        //5 指定使用NIO的传输channel
                    .localAddress(new InetSocketAddress(port))    //6 设置服务端地址
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7 添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                       // 当一个新的连接被接受，一个新的子 Channel 将被创建
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoServerHandler());
                        }
                    });

            ChannelFuture f = b.bind().sync();            //8 绑定服务器，同步进行
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();            //9 同步关闭
        } finally {
            group.shutdownGracefully().sync();            //10
        }
    }

}
