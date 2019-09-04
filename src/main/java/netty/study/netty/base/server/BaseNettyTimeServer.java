package netty.study.netty.base.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class BaseNettyTimeServer {

    public void buildServer(int port){
        //配置服务端的NIO线程组
        //bossGroup线程组用于处理接收客户端发起的FTP请求
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //workGroup线程组用于处理所有接受到的请求的事件和channel的IO
        EventLoopGroup workGroup=new NioEventLoopGroup();
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        /*注意，以child开头的方法都定义在serverBootstrap中，表示处理或配置服务端接收到
        * 的对应的客户端连接的SocketChannel通道*/
        serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                //设置通道的选项参数，这里是针对NioServerSocketChannel而言的
                //option主要针对bossGroup线程组,而childOption主要针对workGroup线程组
                .option(ChannelOption.SO_BACKLOG,1024)
                //处理客户端请求的channel的IO
                /*handler：设置主通道处理器，对于服务端而言就是NioServerSocketChannel，
                * 也就是用来处理Acceptor的操作，对于客户端的NioSocketChannel，主要用来
                * 处理业务操作（此处是childHandler）*/
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ChildChannelHandler());
                    }
                });
        try {
            //绑定端口，同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            //如果调用的方法不是closeFuture()而是close()则客户端永远也连接不上服务端
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放线程池资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        BaseNettyTimeServer server=new BaseNettyTimeServer();
        server.buildServer(7000);
    }
}
