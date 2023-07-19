package com.cytosine.spring.mvc.proxy;



import com.cytosine.spring.mvc.annotation.Transaction;
import com.cytosine.spring.util.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂
 *
 */
public class ProxyFactory {

    //单例模式
    //私有化构造方法
    private ProxyFactory() {

    }
    //提供一个对象
    private static ProxyFactory proxyFactory = new ProxyFactory();

    //提供一个方法获取对象
    public static ProxyFactory getInstance(){
        return proxyFactory;
    }

    //获取一个被代理的对象的代理后的结果
    public Object getProxy(Object target) {

        Object res =  Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object  proxy, Method method, Object[] args) throws Throwable {
                Object result = null;
                Method[] methods = target.getClass().getMethods();
                Transaction annotationM = null; //注解

                //找到同名方法 拿到注解
                for (int i = 0; i < methods.length; i++) {
                    Method method2 = methods[i];
                    if (method2.getName().equals(method.getName())){
                        annotationM = method2.getAnnotation(Transaction.class);
                    }
                }

                //判断是否被注解标记
                if (annotationM != null) {
                    try {
                        //开启事务
                        TransactionManager.getInstance().openTransaction();
                        System.out.println("动态代理成功");
                        result = method.invoke(target, args);
                        //提交事务
                        TransactionManager.getInstance().commitTransaction();
                    } catch (Exception e) {
                        //回滚事务
                        TransactionManager.getInstance().rollbackTransaction();
                        System.out.println("回滚");
                        //将异常抛出
                        throw e;
                    }
                }
                else {
                    result = method.invoke(target,args);
                }
                return result;
            }
        });

        return res;
    }

}
