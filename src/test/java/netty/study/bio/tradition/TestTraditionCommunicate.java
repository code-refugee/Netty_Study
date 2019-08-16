package netty.study.bio.tradition;

import netty.study.bio.tradition.client.TraditionTimeClient;
import netty.study.bio.tradition.server.TraditionTimeServer;
import org.junit.Before;
import org.junit.Test;

public class TestTraditionCommunicate {

    private TraditionTimeServer server;

    private TraditionTimeClient client;

    @Before
    public void beforeTest(){
        server=new TraditionTimeServer();
        client=new TraditionTimeClient();
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
