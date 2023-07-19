package com.cytosine.spring.controller;

import com.cytosine.spring.model.TransferRecord;
import com.cytosine.spring.mvc.annotation.MyAutoWired;
import com.cytosine.spring.service.RecordService;
import com.cytosine.spring.mvc.annotation.MyController;
import com.cytosine.spring.mvc.annotation.MyRequestMapping;
import com.cytosine.spring.mvc.entity.Result;

import java.util.List;

@MyController
@MyRequestMapping("/record")
public class RecordController {

    @MyAutoWired
    private RecordService recordService;

    @MyRequestMapping("/list")
    public Result getRecordList(String accountId){
        List<TransferRecord> list = recordService.getList(accountId);
        return new Result(Result.SUCCESS_CODE, "查询成功", list);
    }

    @MyRequestMapping("/detail")
    public Result getRecordDetail(String id){
        TransferRecord recordDetail = recordService.getRecordDetail(id);
        if (recordDetail != null){
            return new Result(Result.SUCCESS_CODE, "查询成功", recordDetail);
        }else {
            return new Result(Result.FAILED_CODE, "查询失败" );
        }
    }
}
