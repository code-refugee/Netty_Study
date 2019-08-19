package netty.study.bio.improvement.server;

import netty.study.bio.tradition.server.TraditionTimeServerHandle;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* Description: 伪异步I/O编程，使用了线程池对创建的线程数加以控制
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class ImproTimeServer {

    private ServerSocket serverSocket;

    public void bulidServer(int port){
        try {
            serverSocket=new ServerSocket(port);
            ImproTimeServerExecutePool executePool=new ImproTimeServerExecutePool(50,10000);
            System.out.println("The server is start in port:"+port);
            while (true){
                Socket accept = serverSocket.accept();
                executePool.execute(new TraditionTimeServerHandle(accept));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
