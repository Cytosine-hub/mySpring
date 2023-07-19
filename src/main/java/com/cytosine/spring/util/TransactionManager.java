package com.cytosine.spring.util;

import java.sql.SQLException;

/**
 * 事务管理器
 */
public class TransactionManager {

    //单例模式 三步
    //1.私有化构造方法 禁止其他人创建
    private TransactionManager() {
    }
    //创建一个对象
    private static TransactionManager transactionManager = new TransactionManager();

    //提供方法拿到对象
    public static TransactionManager getInstance(){
        return transactionManager;
    }

    //开启事务
    public  void openTransaction(){
        try {
            ConnectionUtil.getCurrentThreadConnection().setAutoCommit(false);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //提交事务
    public  void commitTransaction(){
        try {
            ConnectionUtil.getCurrentThreadConnection().commit();
//            ConnectionUtil.getCurrentThreadConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //事务回滚
    public void rollbackTransaction(){
        try {
            ConnectionUtil.getCurrentThreadConnection().rollback();
//            ConnectionUtil.getCurrentThreadConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
