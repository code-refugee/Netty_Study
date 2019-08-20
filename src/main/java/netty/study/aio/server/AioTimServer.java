package netty.study.aio.server;

import java.util.concurrent.CountDownLatch;

public class AioTimServer {

    public static final CountDownLatch countDownLatch=new CountDownLatch(1);
    public void buildServer(int port){
        AioTimServerHandler serverHandler=new AioTimServerHandler(port);
        new Thread(serverHandler,"aioServer_Thread001").start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
