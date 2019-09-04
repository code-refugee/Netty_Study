package netty.study.netty.decoder.delimiter.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import netty.study.netty.decoder.linebased.server.ChildServerHandler;

public class UseDelimiterTimeServer {

    private String div;

    public UseDelimiterTimeServer(String div) {
        this.div = div;
    }

    public void buildServer(int port){
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        final ByteBuf delimiter= Unpooled.copiedBuffer(div.getBytes());
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter))
                                .addLast(new StringDecoder())
                                .addLast(new ChildServerHandler(div));
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UseDelimiterTimeServer server=new UseDelimiterTimeServer("@|@");
        server.buildServer(7000);
    }
}
