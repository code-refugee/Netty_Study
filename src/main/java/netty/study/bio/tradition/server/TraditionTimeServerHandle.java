package netty.study.bio.tradition.server;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
* Description: 用于对服务端的接收到的socket请求做处理
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/
public class TraditionTimeServerHandle implements Runnable {

    private Socket socket;

    public TraditionTimeServerHandle(Socket accept) {
        this.socket=accept;
    }

    @Override
    public void run() {
        BufferedReader reader=null;
        PrintWriter writer=null;
        try {
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new PrintWriter(socket.getOutputStream(),true);
            String receive;
            String send;
            while (true){
                if((receive=reader.readLine())!=null){
                    System.out.println("Server receive client msg:"+receive);
                    send=receive.equalsIgnoreCase("QUERY")?"TELL YOU TIME" :"BAD QUERY";
                    writer.println(send);
                }else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(socket);
        }
    }
}
