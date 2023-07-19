package com.cytosine.spring.listener;




import com.cytosine.spring.core.ServerThread;
import com.cytosine.spring.mvc.proxy.ProxyFactory;
import com.cytosine.spring.service.AccountService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CoreServerListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServerThread serverThread = new ServerThread();

        serverThread.setAccountService(
                (AccountService) ProxyFactory.getInstance().getProxy(serverThread.getAccountService())
        );

        serverThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
