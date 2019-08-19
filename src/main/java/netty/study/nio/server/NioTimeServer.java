package netty.study.nio.server;

/**
* Description:使用NIO创建socket服务
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class NioTimeServer {

    public void buildServer(int port){
        MultipleTimeServer server=new MultipleTimeServer(port);
        new Thread(server,"NIO-MultipleTimeServer-001").start();
    }
}
