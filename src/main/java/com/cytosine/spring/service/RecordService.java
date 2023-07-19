package com.cytosine.spring.service;

import com.cytosine.spring.model.TransferRecord;

import java.util.List;

public interface RecordService {
    public List<TransferRecord> getList(String accountId);

    public TransferRecord getRecordDetail(String id);
}
