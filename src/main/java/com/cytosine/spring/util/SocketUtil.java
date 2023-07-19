package com.cytosine.spring.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 报文发送工具类
 */
public class SocketUtil {
    private static Socket socket = null;

    public SocketUtil(String ip,int host) throws UnknownHostException, IOException {
        socket = new Socket(ip,host);
    }

    /**
     * 发送XML报文
     * @param reqXml
     * @return
     */
    public static String sendXmlSocket(String reqXml){
        String resXml = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(reqXml);
            writer.flush();
            resXml = in.readLine();
            in.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resXml;
    }



}
