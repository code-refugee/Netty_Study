package netty.study.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AioTimServerHandler implements Runnable {

    private AsynchronousServerSocketChannel serverSocketChannel;

    private CountDownLatch latch;

    public AioTimServerHandler(int port) {
        try {
            serverSocketChannel=AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            latch=new CountDownLatch(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        //注意这里并没有使用while循环
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            AioTimServer.countDownLatch.countDown();
        }
    }

    private void doAccept() {
        serverSocketChannel.accept(this,new AioServerAcceptCom());
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
