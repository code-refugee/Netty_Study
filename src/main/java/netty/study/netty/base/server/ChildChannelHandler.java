package netty.study.netty.base.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChildChannelHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) {
        ByteBuf byteBuf=(ByteBuf)o;
        byte[] readByte=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(readByte);
        System.out.println("netty服务端接收到的消息:"+new String(readByte));
        byte[] serverSend="呸，我就是死，也不当汉奸".getBytes();
        ByteBuf sendBuf= Unpooled.copiedBuffer(serverSend);
        ctx.write(sendBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
