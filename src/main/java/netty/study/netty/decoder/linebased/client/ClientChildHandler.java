package netty.study.netty.decoder.linebased.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientChildHandler extends SimpleChannelInboundHandler {

    private String div;

    private int count=0;

    public ClientChildHandler(String div){
        this.div=div;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("这是客户端接收的第"+(++count)+"条："+o);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] send=("Is my answer correct"+div).getBytes();
        for (int i = 0; i < 1000; i++) {
            ByteBuf writeBuf= Unpooled.copiedBuffer(send);
            ctx.writeAndFlush(writeBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
