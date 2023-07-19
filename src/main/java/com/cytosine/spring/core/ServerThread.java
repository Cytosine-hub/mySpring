package com.cytosine.spring.core;

import com.cytosine.spring.service.AccountService;
import com.cytosine.spring.service.impl.AccountServiceImpl;
import com.cytosine.spring.model.Response;
import com.cytosine.spring.model.ResponseHead;

import com.cytosine.spring.model.StreamRecord;
import com.cytosine.spring.util.JaxbUtil;
import com.cytosine.spring.util.RequestType;



public class ServerThread extends Thread{

    private static AccountService accountService = new AccountServiceImpl();

    @Override
    public void run() {
        PoolServer server = new PoolServer(20,8083);
        //初始化
        server.setupHandlers();
        //接受请求
        server.acceptConnection();
    }

    public String handleBuss(String type, Response req){
        ResponseHead responseHead = new ResponseHead();
        Response response = new Response();


        //查询业务处理
        if(RequestType.QUERY_RECORD.equals(type)){
        }
        //转账业务处理
        if (RequestType.TRANSFER.equals(type)){
            StreamRecord body = (StreamRecord) req.getBody();

            String s = "";
            try {
                s = accountService.transferCore(body.getInAccountId(), body.getPayAccountId(), body.getState(), body.getTransMoney());
            } catch (Exception e) {
                e.printStackTrace();
                s = "转账失败！";
            }

            if ("".equals(s)){
                responseHead.setCode(RequestType.SUCCESS);
            }else {
                responseHead.setCode(RequestType.FAIL);
                responseHead.setMessage(s);
            }
            response.setHead(responseHead);
        }
        return JaxbUtil.convertToXml(response,responseHead.getClass());
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        ServerThread.accountService = accountService;
    }
}
