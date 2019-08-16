package netty.study.bio.tradition.server;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* Description: 这是一个传统的同步阻塞式I/O，也是最常见的
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class TraditionTimeServer {

    private ServerSocket serverSocket;

    public void buildServer(int port){
        try {
            serverSocket=new ServerSocket(port);
            System.out.println("The server is start in port:"+port);
            while (true){
                Socket accept = serverSocket.accept();
                new Thread(new TraditionTimeServerHandle(accept)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(serverSocket);
        }
    }
}
