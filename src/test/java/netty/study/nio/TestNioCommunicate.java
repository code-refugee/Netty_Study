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

    @Test
    public void createServer(){
        server.buildServer(7000);
    }

    @Test
    public void createClient(){
        client.buildClient("127.0.0.1",7000);
    }
}
