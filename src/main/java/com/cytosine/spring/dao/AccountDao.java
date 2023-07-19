package com.cytosine.spring.dao;

import com.cytosine.spring.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public BigDecimal getAccountMoney(String accountId);

    public Account getAccountInfo(String accountId);

    public int setMoney(String accountId,BigDecimal money);

}
