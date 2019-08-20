package netty.study.nio.server;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class MultipleTimeServer implements Runnable {

    private ServerSocketChannel socketChannel;

    //观察者
    private Selector selector;

    private volatile boolean stop=false;

    public MultipleTimeServer(int port) {
        try {
            selector=Selector.open();
            socketChannel=ServerSocketChannel.open();
            //设为非阻塞式通信
            socketChannel.configureBlocking(false);
            //bind()方法的第二个参数为套接字上请求的最大挂起连接数
            socketChannel.socket().bind(new InetSocketAddress(port),1024);
            //在通道上注册观察者，让观察者观察接收操作
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The server is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop=true;
    }

    @Override
    public void run() {
        while (!stop){
            try {
                //设置轮询时间，这里设置为每隔1s轮询一次
                selector.select(1000);
                //一旦轮询到一个channel有所注册的事情发生了，它就会交出钥匙，我们通过钥匙去读取内容
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key=null;
                while (iterator.hasNext()){
                    key=iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if(key!=null)
                            key.cancel();
                        if(key.channel()!=null)
                            key.channel().close();
                    }
                }

            } catch (IOException e) {

            }
        }

        if(selector!=null){
            //多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去掉注册并关闭
            IOUtils.closeQuietly(selector);
            NioTimeServer.latch.countDown();
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        //检测该密匙是否有效
        if(key.isValid()){
            //测试此密匙的通道是否已准备好接受新的套接字连接
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //客户端建立的连接
                SocketChannel sc=ssc.accept();
                sc.configureBlocking(false);
                //在通道上注册观察者，让观察者观察客户端读操作
                sc.register(selector,SelectionKey.OP_READ);
            }
            //测试此密匙的频道是否可以读
            if(key.isReadable()){
                SocketChannel sc= (SocketChannel) key.channel();
                //分配一个新的字节缓冲区
                ByteBuffer reader=ByteBuffer.allocate(1024);
                int readBytes = sc.read(reader);
                if(readBytes>0){
                    /*一定得有,如果没有,就是从文件最后开始读取的,
                    * 通过这个方法，就能把buffer的当前位置更改为buffer缓冲区的第一个位置*/
                    reader.flip();
                    byte[] bytes=new byte[reader.remaining()];
                    reader.get(bytes);
                    System.out.println("Server receive client msg:"+new String(bytes,"UTF-8"));
                    doWrite(sc);
                } else if(readBytes<0){
                    //取消该密钥的通道与其选择器的注册
                    key.cancel();
                    IOUtils.closeQuietly(sc);
                }
            }
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] bytes="SUCCESS CONNECT SERVER".getBytes();
        ByteBuffer write=ByteBuffer.allocate(bytes.length);
        write.put(bytes);
        write.flip();
        sc.write(write);
    }
}
