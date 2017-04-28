package net.cocloud.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Handler implementation for the echo client.
 * It initiates the ping-pong traffic between the echo client and server
 * by sending the a fixed msg prefix and a random integer.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    private static final String HI_MSG = "hello netty ";
    private static final int MSG_CAPACITY = 50;
    private static final int INTERVAL_SECONDS = 5;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.buffer(MSG_CAPACITY).writeBytes(HI_MSG.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        logger.info("receive msg from server: {}", new String(bytes, StandardCharsets.UTF_8));

        try {
            TimeUnit.SECONDS.sleep(INTERVAL_SECONDS);
            String sendMsg = HI_MSG + new Random().nextInt();
            ctx.write(Unpooled.buffer(MSG_CAPACITY).writeBytes((sendMsg).getBytes()));
            logger.info("send msg to server: {}", sendMsg);
        }
        catch (InterruptedException ignore) {

        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}