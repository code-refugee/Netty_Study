package netty.study.bio.tradition.client;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
* Description: 同步阻塞式I/O创建的客户端
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class TraditionTimeClient {

    public void buildClient(String ip,int port){
        BufferedReader reader=null;
        PrintWriter writer=null;
        Socket clientSocket=null;
        try {
            clientSocket=new Socket(ip,port);
            reader=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer=new PrintWriter(clientSocket.getOutputStream(),true);
            writer.println("QUERY");
            System.out.println("client send a msg");
            String receive;
            while ((receive=reader.readLine())!=null){
                System.out.println("client receive:"+receive);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(clientSocket);
        }
    }
}
