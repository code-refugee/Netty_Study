package netty.study.aio.client;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AioTimeClientHandler implements Runnable, CompletionHandler<Void,AioTimeClientHandler> {

    private AsynchronousSocketChannel socketChannel;

    private CountDownLatch latch;

    private String ip;

    private int port;

    public AioTimeClientHandler(String ip,int port) {
        this.ip=ip;
        this.port=port;
        try {
            socketChannel=AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        latch=new CountDownLatch(1);
        socketChannel.connect(new InetSocketAddress(ip,port),this,this);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AioTimeClientHandler attachment) {
        final byte[] bytes="Aio客户端说你好恶心啊".getBytes();
        ByteBuffer writeByte=ByteBuffer.allocate(bytes.length);
        writeByte.put(bytes);
        writeByte.flip();
        socketChannel.write(writeByte, writeByte, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if(attachment.hasRemaining())
                    socketChannel.write(attachment,attachment,this);
                else {
                    ByteBuffer readByte=ByteBuffer.allocate(1024);
                    socketChannel.read(readByte, readByte, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] rece=new byte[attachment.remaining()];
                            attachment.get(rece);
                            System.out.println("客户端接收的消息："+new String(rece));
                            latch.countDown();
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            IOUtils.closeQuietly(socketChannel);
                            latch.countDown();
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                IOUtils.closeQuietly(socketChannel);
                latch.countDown();
            }
        });
    }

    @Override
    public void failed(Throwable exc, AioTimeClientHandler attachment) {
        IOUtils.closeQuietly(socketChannel);
        latch.countDown();
    }
}
