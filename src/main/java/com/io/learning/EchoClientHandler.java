/**
 * Author: Tang Yuqian
 * Date: 2020/11/22
 */
package com.io.learning;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable                                //1
public class EchoClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 建立连接后该 channelActive() 方法被调用一次
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2 当被通知该 channel 是活动的时候就发送信息
                CharsetUtil.UTF_8));
    }

    /**
     * 这种方法会在接收到数据时被调用
     * @param ctx
     * @param in
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx,
                             ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //3
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {                    //4
        cause.printStackTrace();
        ctx.close();
    }
}
