package netty.study.nio.server;

import java.util.concurrent.CountDownLatch;

/**
* Description:使用NIO创建socket服务
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class NioTimeServer {
    //倒计数器，用于控制多线程执行
    public static final CountDownLatch latch=new CountDownLatch(1);

    public void buildServer(int port){
        MultipleTimeServer server=new MultipleTimeServer(port);
        new Thread(server,"NIO-MultipleTimeServer-001").start();
        try {
            //若上面线程一直未调用countDown()方法则该线程会一直等待
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
