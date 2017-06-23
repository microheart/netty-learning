package com.iknowers.learning.netty.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * Handles a client-side channel.
 */
@Sharable
public class PacketClientHandler extends ChannelInboundHandlerAdapter {

    private static final String[] data = new String[] {"hello ",
            ", It is netty4. $$ welcome, ",
            "enjoying $$"};

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (String msg: data) {
            ByteBuf buf = Unpooled.buffer(msg.length());
            buf.writeBytes(msg.getBytes());
            ctx.writeAndFlush(buf);

            TimeUnit.SECONDS.sleep(2);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("[Client] received: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
