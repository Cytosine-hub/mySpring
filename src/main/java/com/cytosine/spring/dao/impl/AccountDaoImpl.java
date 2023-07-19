package com.cytosine.spring.dao.impl;

import com.cytosine.spring.dao.AccountDao;
import com.cytosine.spring.util.DatabaseUtil;
import com.cytosine.spring.model.Account;

import java.math.BigDecimal;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    @Override
    public BigDecimal getAccountMoney(String accountId) {
        String sql = "select * from account where account_id = ?";
        List<Account> bigDecimals = DatabaseUtil.executeQuery(Account.class, sql, accountId);

        if (bigDecimals.isEmpty()){
            return null;
        }
        return bigDecimals.get(0).getMoney();
    }

    @Override
    public Account getAccountInfo(String accountId) {
        String sql = "select * from account where account_id = ?";
        List<Account> accounts = DatabaseUtil.executeQuery(Account.class, sql, accountId);
        return accounts.get(0);
    }

    @Override
    public int setMoney(String accountId,BigDecimal money) {
        String sql = "update account set money=? WHERE account_id=?";
        int i = DatabaseUtil.executeUpdate(Account.class, sql, money, accountId);
        return i;
    }


}
