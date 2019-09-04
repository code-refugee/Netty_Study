package netty.study.netty.decoder.linebased.server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChildServerHandler extends SimpleChannelInboundHandler {

    private int count=0;

    private String div;

    public ChildServerHandler(String div){
        this.div=div;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("这是服务端接收到的第"+(++count)+"条："+o);
        String send=(o.equals("Is my answer correct")?"right":"error")+div;
        byte[] sendByte=send.getBytes();
        ByteBuf writeBuf= Unpooled.buffer(sendByte.length);
        writeBuf.writeBytes(sendByte);
        channelHandlerContext.writeAndFlush(writeBuf);
    }
}
