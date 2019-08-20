package netty.study.nio;

import netty.study.nio.server.NioTimeServer;
import netty.study.nio.client.NioTimeClient;
import org.junit.Before;
import org.junit.Test;

public class TestNioCommunicate {

    private NioTimeServer server;

    private NioTimeClient client;

    @Before
    public void doBefore(){
        server=new NioTimeServer();
        client=new NioTimeClient();
    }

    /*该方法是单元测试线程，不是守护线程，当该线程结束之后
    * 子线程立即结束。main方法是缺省的，jvm虚拟机会等待所有线程结束*/
    @Test
    public void createServer(){
        server.buildServer(7000);
    }

//    public static void main(String[] args) {
//        NioTimeServer server=new NioTimeServer();
//        server.buildServer(7000);
//    }

    @Test
    public void createClient(){
        client.buildClient("127.0.0.1",7000);
    }
}
