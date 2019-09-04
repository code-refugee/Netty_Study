package netty.study.netty.base.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 继承SimpleChannelInboundHandler的好处是，它会自动释放资源，不需要再去
 * 调用ReferenceCountUtil.release() 方法。所以，你不应该存储指向任何消息
 * 的引用供将来使用，因为这些引用都将会失效
 * **/
public class ClientChannelHandler extends SimpleChannelInboundHandler {

    private int count=0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //若服务端是以ButeBuf发送信息的话，客户端接收到的也是ByteBuf类型的
        if (o instanceof ByteBuf){
            ByteBuf readByte=(ByteBuf)o;
            byte[] bytes=new byte[readByte.readableBytes()];
            readByte.readBytes(bytes);
            System.out.println("netty客户端接收到第"+(++count)+"的消息："+new String(bytes));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] send="is my answer correct".getBytes();
        for (int i = 0; i < 1000; i++) {
            ByteBuf sendMsg= Unpooled.buffer(send.length);
            sendMsg.writeBytes(send);
            ctx.writeAndFlush(sendMsg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
