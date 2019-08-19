package netty.study.nio.client;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultipleTimeClient implements Runnable {

    private String ip;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop = false;

    public MultipleTimeClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null)
                                key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            IOUtils.closeQuietly(selector);
        }

    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                //完成连接插座通道的过程
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite();
                } else
                    System.exit(1);
            }
            if(key.isReadable()){
                ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
                int readBytes=socketChannel.read(byteBuffer);
                if(readBytes>0){
                    byteBuffer.flip();
                    byte[] bytes=new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    System.out.println("客户端接收："+new String(bytes));
                    stop();
                } else if(readBytes<0){
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        //如果直接连接成功，则注册到多路复用器上，发送请求信息，读应答
        if (socketChannel.connect(new InetSocketAddress(ip, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite();
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite() throws IOException {
        byte[] bytes = "NIO_CLIENT SEND A MSG".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        //检测byteBuffer中是否还有遗留的字节
        if (!byteBuffer.hasRemaining()) {
            System.out.println("CLIENT SEND SUCCESS");
        }
    }
}
