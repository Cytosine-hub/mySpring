package com.cytosine.spring.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 创建一个线程池 处理客户端的请求
 */
public class PoolServer {
    //最大连接数
    protected int maxConnection;
    //监听端口
    protected int listenPort;
    //服务端
    protected ServerSocket serverSocket = null;

    //构造器 端口和连接数
    public PoolServer(int maxConnection, int listenPort) {
        this.maxConnection = maxConnection;
        this.listenPort = listenPort;
    }


    //1.初始化线程池（关于请求的处理器） 创建线程
    public void setupHandlers(){
        for (int i = 0; i < maxConnection; i++) {
            //创建一个线程
            PooledConnectionHandler pooledConnectionHandler = new PooledConnectionHandler();
            System.out.println("创建server process线程" + i);
            //启动
            new Thread(pooledConnectionHandler).start();
        }
    }
    //2.接受客户端的连接
    public void acceptConnection(){
        try {
            serverSocket = new ServerSocket(listenPort);
            //接受请求
           while (true){
               Socket incomeConnection = serverSocket.accept();
               processConnection(incomeConnection);
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //3.处理客户端的请求
    protected void processConnection(Socket incomeConnection){
        //有一个请求就生产一个对象等待处理
        PooledConnectionHandler.processRequest(incomeConnection);
    }

}
