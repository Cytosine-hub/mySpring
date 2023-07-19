package com.cytosine.spring.util;

import java.sql.Connection;

/**
 * 从ThreadLocal中获取一个连接对象
 * 在一个线程中使用的是同一个对象
 */
public class ConnectionUtil {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal();

    //从threadLocal中获取一个connection
    public static Connection getCurrentThreadConnection(){

        Connection connection = threadLocal.get();
        if (connection == null){
            Connection con = null;
            try {
                con = ConnectionPool.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            threadLocal.set(con);
            connection = threadLocal.get();
        }

        return connection;
    }
}
