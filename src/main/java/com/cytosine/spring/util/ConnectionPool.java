package com.cytosine.spring.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 实现一个简单版本的mybatis
 */
public class ConnectionPool {
    //连接数最大数量，可以新建但是不能进队列
    private static final int MAX_SIZE = 30;
    private static final int CORE_SIZE = 10;
    //当前连接数
    private static int CURRENT_SIZE = 0;
    private static String username="root";
    private static String password="123456";
    private static String url="jdbc:mysql://localhost:3306/db_exam2?serverTimezone=Asia/Shanghai";
    //final不可改变，保证连接池唯一
    private static final LinkedBlockingDeque<Connection> connections = new LinkedBlockingDeque<>(CORE_SIZE);
    private static final String GET_LOCK_SIGN = "GET_LOCK_SING";
    private static final String PUT_LOCK_SIGN = "PUT_LOCK_SING";

    /**
     * 别人不能实例化 饿汉模式
     */
    private ConnectionPool(){

    }

    //类第一次加载的时候执行
    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < CORE_SIZE; i++) {
                connections.add(init());
            }
        }catch (Exception e){

        }
    }
    //初始化一个连接到连接池
    private static Connection init() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url,username,password);
        return connection;
    }

    //在方法上加锁，实际上是锁类对象,锁代码块
    public static Connection get() throws Exception{
        synchronized (GET_LOCK_SIGN){
            Connection pop;
            //2.不关心对当前连接数，直接创建连接并返回
            if (connections != null){
                if (connections.size() !=0){
                    pop = connections.pop();
                }else {
                    pop = init();
                }
            }else {
                pop = init();
            }
            CURRENT_SIZE += 1;
            return pop;
        }
    }

    public static void put(Connection connection) throws SQLException{
        //判断当前连接数是否已经达到最大连接数，达到则销毁
        synchronized (PUT_LOCK_SIGN){
            if (CURRENT_SIZE > CORE_SIZE){
                connection.close();
                CURRENT_SIZE -= 1;
            }else {
                connections.push(connection);
            }
        }
    }


}
