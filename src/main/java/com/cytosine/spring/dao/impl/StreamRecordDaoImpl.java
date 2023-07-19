package com.cytosine.spring.dao.impl;

import com.cytosine.spring.dao.StreamRecordDao;
import com.cytosine.spring.model.StreamRecord;
import com.cytosine.spring.util.DatabaseUtil;

public class StreamRecordDaoImpl implements StreamRecordDao {
    @Override
    public int addRecord(StreamRecord record) {
        String sql = "insert into stream_record(id,type,option_id,pay_account_id,in_account_id,trans_money,trans_time,state) " +
                "values(?,?,?,?,?,?,?,?)";
        int i = DatabaseUtil.executeUpdate(StreamRecord.class,
                sql, record.getId(),record.getType(), record.getOptionId(), record.getPayAccountId(), record.getInAccountId(), record.getTransMoney(), record.getTransTime(), record.getState());
        return i;
    }

    @Override
    public int updateState(String id, String state) {
        String sql = "update stream_record set state = ? where id = ?";
        int i = DatabaseUtil.executeUpdate(StreamRecord.class, sql, state, id);
        return i;
    }
}
