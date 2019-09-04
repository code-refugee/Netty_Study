package netty.study.netty.decoder.delimiter.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import netty.study.netty.decoder.linebased.client.ClientChildHandler;

public class UseDelimiterTimeClient {

    private String div;

    public UseDelimiterTimeClient(String div) {
        this.div = div;
    }

    public void buildClient(String ip,int port){
        EventLoopGroup workGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        final ByteBuf delimiter= Unpooled.copiedBuffer(div.getBytes());
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter))
                                .addLast(new StringDecoder())
                                .addLast(new ClientChildHandler(div));
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UseDelimiterTimeClient client=new UseDelimiterTimeClient("@|@");
        client.buildClient("127.0.0.1",7000);
    }
}
