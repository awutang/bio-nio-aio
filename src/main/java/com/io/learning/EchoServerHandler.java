/**
 * Author: Tang Yuqian
 * Date: 2020/11/22
 */
package com.io.learning;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
// import org.jboss.netty.util.CharsetUtil; // netty在4.0.0之后结构发生了变化

/**
 * Echo Server 将接受到的数据的拷贝发送给客户端
 */
@ChannelHandler.Sharable                                        //1 ChannelHandler的实例可以在channel之间共享
public class EchoServerHandler extends
        ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));        //2
        ctx.write(in);                            //3 将接受到read的msg返回write给发送者
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//4 flush所有的消息到远程节点
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();                //5
        ctx.close();                            //6 关闭通道
    }
}
