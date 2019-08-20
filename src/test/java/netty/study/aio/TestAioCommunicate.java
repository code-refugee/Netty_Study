package netty.study.aio;

import netty.study.aio.client.AioTimeClient;
import netty.study.aio.server.AioTimServer;
import org.junit.Before;
import org.junit.Test;

public class TestAioCommunicate {

    private AioTimServer aioTimServer;


    @Before
    public void doBefore(){
        aioTimServer=new AioTimServer();
    }

    @Test
    public void createServer(){
        aioTimServer.buildServer(7000);
    }

    public static void main(String[] args) {
        AioTimeClient aioTimeClient=new AioTimeClient();
        aioTimeClient.buildClient("127.0.0.1",7000);
    }
}
