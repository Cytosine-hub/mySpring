package com.cytosine.spring.dao.impl;

import com.cytosine.spring.model.TransferRecord;
import com.cytosine.spring.dao.TransferRecordDao;
import com.cytosine.spring.util.DatabaseUtil;

import java.util.List;

public class TransferRecordDaoImpl implements TransferRecordDao {

    @Override
    public List<TransferRecord> getRecordListByPayId(String accountId) {
        String sql = "select * from transfer_record where pay_account_id = ?";
        List<TransferRecord> transferRecords = DatabaseUtil.executeQuery(TransferRecord.class, sql, accountId);

        return transferRecords;
    }

    @Override
    public List<TransferRecord> getRecordListByInId(String accountId) {
        String sql = "select * from transfer_record where in_account_id = ?";

        List<TransferRecord> transferRecords = DatabaseUtil.executeQuery(TransferRecord.class, sql, accountId);

        return transferRecords;
    }

    @Override
    public TransferRecord getDetailById(String id) {
        String sql = "select * from transfer_record where id = ?";

        List<TransferRecord> transferRecords = DatabaseUtil.executeQuery(TransferRecord.class, sql, id);

        return transferRecords.get(0);
    }

    @Override
    public int addRecord(TransferRecord record) {
        String sql = "insert into transfer_record(id,pay_account_id,in_account_id,money,trans_time) " +
                "values(?,?,?,?,NOW())";
//        System.out.println(1/0);
        int i = DatabaseUtil.executeUpdate(TransferRecord.class, sql,
                record.getId(),record.getPayAccountId(),record.getInAccountId(),record.getMoney());
        return i;
    }
}
