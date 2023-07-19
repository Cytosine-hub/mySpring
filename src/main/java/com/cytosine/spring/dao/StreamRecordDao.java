package com.cytosine.spring.dao;

import com.cytosine.spring.model.StreamRecord;

public interface StreamRecordDao {

    public int addRecord(StreamRecord record);

    public int updateState(String id,String state);


}
