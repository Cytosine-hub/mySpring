package com.cytosine.spring.service;

import java.math.BigDecimal;

public interface AccountService {

    public String transfer(String mobile,String inId, String outId, String payPassword, BigDecimal money);

    public String transferCore(String inId, String outId, String payPassword, BigDecimal money);

    public BigDecimal getRestMoney(String accountId);

}
