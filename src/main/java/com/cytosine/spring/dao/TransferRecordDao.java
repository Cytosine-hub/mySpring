package com.cytosine.spring.dao;

import com.cytosine.spring.model.TransferRecord;

import java.util.List;

public interface TransferRecordDao {

    public List<TransferRecord> getRecordListByPayId(String accountId);

    public List<TransferRecord> getRecordListByInId(String accountId);


    public TransferRecord getDetailById(String id);

    public int addRecord(TransferRecord record);
}
