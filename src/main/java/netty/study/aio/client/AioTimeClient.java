package netty.study.aio.client;


public class AioTimeClient {

    public void buildClient(String ip,int port){
        AioTimeClientHandler clientHandler=new AioTimeClientHandler(ip,port);
        new Thread(clientHandler,"aioClient_Thread002").start();
    }
}
