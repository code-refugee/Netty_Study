package netty.study.netty.base;

import netty.study.netty.base.client.BaseNettyTimeClient;
import netty.study.netty.base.server.BaseNettyTimeServer;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


public class TestNettyBase {

    private CountDownLatch latch=new CountDownLatch(1);

    private BaseNettyTimeServer server;

    private BaseNettyTimeClient client;

    @Test
    public void buildServer(){
        server=new BaseNettyTimeServer();
        server.buildServer(8080);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BaseNettyTimeClient client=new BaseNettyTimeClient();
        client.buildClient("127.0.0.1",7000);
    }
}
