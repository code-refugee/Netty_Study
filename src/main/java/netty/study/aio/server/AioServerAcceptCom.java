package netty.study.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioServerAcceptCom implements CompletionHandler<AsynchronousSocketChannel,AioTimServerHandler> {
    //成功接收到客户端访问后读取客户端数据
    @Override
    public void completed(AsynchronousSocketChannel result, AioTimServerHandler attachment) {
        //因为服务端要接受的客户端不止一个，肯能有几千几万个，所以这里要循环调用accept
        //每当接收一个客户读连接成功后，再异步接受新的客户端连接
        attachment.getServerSocketChannel().accept(attachment,this);
        ByteBuffer readByte=ByteBuffer.allocate(1024);
        result.read(readByte, readByte, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AioTimServerHandler attachment) {
        attachment.getLatch().countDown();
    }
}
