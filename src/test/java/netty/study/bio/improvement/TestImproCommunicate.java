package netty.study.bio.improvement;

import netty.study.bio.improvement.server.ImproTimeServer;
import netty.study.bio.tradition.client.TraditionTimeClient;
import org.junit.Before;
import org.junit.Test;

public class TestImproCommunicate {

    private ImproTimeServer server;

    private TraditionTimeClient client;

    @Before
    public void testBefore(){
        server=new ImproTimeServer();
        client=new TraditionTimeClient();
    }

    @Test
    public void buildServer(){
        server.bulidServer(7000);
    }

    @Test
    public void bulidClient(){
        client.buildClient("127.0.0.1",7000);
    }
}
