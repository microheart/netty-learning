package com.iknowers.learning.netty.packet;

import com.iknowers.learning.netty.telnet.CodecFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Simplistic telnet server.
 */
public final class PacketServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8023"));

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline pipeline = ch.pipeline();

                     pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Unpooled.wrappedBuffer("$$".getBytes())));

                     // the encoder and decoder are static as these are sharable
                     pipeline.addLast(CodecFactory.STRING_DECODER);
                     pipeline.addLast(CodecFactory.STRING_ENCODER);

                     // and then business logic.
                     pipeline.addLast(new PacketServerHandler());
                 }
             });

            b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
