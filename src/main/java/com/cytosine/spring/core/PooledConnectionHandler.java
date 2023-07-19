package com.cytosine.spring.core;




import com.cytosine.spring.util.JaxbUtil;

import com.cytosine.spring.model.Response;
import com.cytosine.spring.model.StreamRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class PooledConnectionHandler implements Runnable{
    protected static List Pool = new LinkedList();   //类变量 用来存放所有的请求对象
    protected Socket connection;

    public PooledConnectionHandler() {

    }

    //消费者
    @Override
    public void run() {
        //while保证线程一直存在
        while (true){
            //线程安全
            synchronized (Pool){
                //如果没有请求就一直等待
                while (Pool.isEmpty()){
                    try {
                        Pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //先拿一个请求过来
                connection = (Socket) Pool.remove(0);
                //拿完了就处理
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    String fromClient = in.readLine();
                    System.out.println(fromClient);
                    Response req = JaxbUtil.convertToJavaBean(fromClient,Response.class, StreamRecord.class);
                    //获取请求类型
                    String type = req.getHead().getType();
                    //业务处理
                    ServerThread serverThread = new ServerThread();
                    String res = serverThread.handleBuss(type,req);
                    //发送回复
                    out.println(res);
                    out.flush();

                    //处理结束后释放资源
                    connection.close();
                    in.close();
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    //生产者 类方法
    public static void processRequest(Socket connection){
        synchronized (Pool){
            //在最后加
            Pool.add(Pool.size(),connection);
            //唤醒所有线程进行处理
            Pool.notifyAll();
        }
    }
}
