package com.cytosine.spring.controller;

import com.cytosine.spring.mvc.annotation.MyAutoWired;
import com.cytosine.spring.service.AccountService;
import com.cytosine.spring.mvc.annotation.MyController;
import com.cytosine.spring.mvc.annotation.MyRequestMapping;
import com.cytosine.spring.mvc.entity.Result;

import java.math.BigDecimal;

@MyController
@MyRequestMapping("/account")
public class AccountController {

    @MyAutoWired
    private AccountService accountService;

    @MyRequestMapping("/transfer")
    public Result transfer(String mobile,String inAccountId,String outAccountId,String payPassword,String money){
        BigDecimal transMoney = new BigDecimal(money).setScale(10,BigDecimal.ROUND_HALF_EVEN);
        if (inAccountId.equals(outAccountId)){
            return new Result(Result.FAILED_CODE, "转账失败！");
        }
        String transferMessage = accountService.transfer(mobile, inAccountId, outAccountId, payPassword, transMoney);

        if (transferMessage == null || "".equals(transferMessage)){
            return new Result(Result.SUCCESS_CODE, "转账成功！");
        }else {
            return new Result(Result.FAILED_CODE, transferMessage);
        }
    }
    @MyRequestMapping("/restMoney")
    public Result accountMoney(String accountId){
        BigDecimal restMoney = accountService.getRestMoney(accountId);
        if (restMoney == null){
            return new Result(Result.FAILED_CODE, "查询失败！");
        }
        return new Result(Result.SUCCESS_CODE, "查询成功！", restMoney);
    }
}
