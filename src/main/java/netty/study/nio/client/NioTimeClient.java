package netty.study.nio.client;

/**
* Description:使用NIO创建client
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class NioTimeClient {

    public void buildClient(String ip,int port){
        new Thread(new MultipleTimeClient(ip,port)).start();
    }
}
