package com.cytosine.spring.service.impl;

import com.cytosine.spring.model.TransferRecord;
import com.cytosine.spring.service.RecordService;
import com.cytosine.spring.dao.TransferRecordDao;
import com.cytosine.spring.dao.impl.TransferRecordDaoImpl;
import com.cytosine.spring.mvc.annotation.MyService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@MyService
public class RecordServiceImpl implements RecordService {
    private TransferRecordDao transferRecordDao = new TransferRecordDaoImpl();


    @Override
    public List<TransferRecord> getList(String accountId) {
        List<TransferRecord> records = new LinkedList<>();

        List<TransferRecord> recordListByInId = transferRecordDao.getRecordListByInId(accountId);
        for (TransferRecord record : recordListByInId) {
            record.setType("收入");
        }
        List<TransferRecord> recordListByPayId = transferRecordDao.getRecordListByPayId(accountId);
        for (TransferRecord record : recordListByPayId) {
            record.setType("支出");
        }
        records.addAll(recordListByInId);
        records.addAll(recordListByPayId);
        records.sort(Comparator.comparing(TransferRecord::getTransTime).reversed());

        return records;
    }

    @Override
    public TransferRecord getRecordDetail(String id) {
        return transferRecordDao.getDetailById(id);
    }
}
