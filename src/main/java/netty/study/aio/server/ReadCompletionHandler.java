package netty.study.aio.server;

import org.apache.commons.io.IOUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements java.nio.channels.CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        if (this.socketChannel==null) {
            this.socketChannel = socketChannel;
        }
    }

    //成功读取到客户端数据后，打印到控制台并返回给客户端信息
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] bytes=new byte[attachment.remaining()];
        attachment.get(bytes);
        System.out.println("AIO服务端接收到的信息为："+new String(bytes));
        doWrite();
    }

    private void doWrite() {
        byte[] bytes="AIO服务端向你问好".getBytes();
        ByteBuffer writeByte=ByteBuffer.allocate(bytes.length);
        writeByte.put(bytes);
        writeByte.flip();
        socketChannel.write(writeByte, writeByte, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                //如果没有发送完成则继续发送
                if(attachment.hasRemaining())
                    socketChannel.write(attachment,attachment,this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                IOUtils.closeQuietly(socketChannel);
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        IOUtils.closeQuietly(socketChannel);
    }
}
