package com.cytosine.spring.service.impl;

import com.cytosine.spring.dao.TransferRecordDao;
import com.cytosine.spring.dao.impl.StreamRecordDaoImpl;
import com.cytosine.spring.dao.impl.TransferRecordDaoImpl;
import com.cytosine.spring.model.TransferRecord;
import com.cytosine.spring.mvc.annotation.Transaction;
import com.cytosine.spring.dao.AccountDao;
import com.cytosine.spring.dao.StreamRecordDao;
import com.cytosine.spring.dao.impl.AccountDaoImpl;
import com.cytosine.spring.model.Response;
import com.cytosine.spring.model.ResponseHead;
import com.cytosine.spring.model.StreamRecord;
import com.cytosine.spring.mvc.annotation.MyService;
import com.cytosine.spring.service.AccountService;
import com.cytosine.spring.util.GenerateBankCardID;
import com.cytosine.spring.util.JaxbUtil;
import com.cytosine.spring.util.RequestType;
import com.cytosine.spring.util.SocketUtil;

import java.math.BigDecimal;
import java.util.Date;

@Transaction
@MyService
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao = new AccountDaoImpl();
    private StreamRecordDao streamRecordDao = new StreamRecordDaoImpl();
    private TransferRecordDao transferRecordDao = new TransferRecordDaoImpl();

    @Override
    public String transfer(String mobile,String inId, String outId, String payPassword, BigDecimal money) {
        String streamRecordId = GenerateBankCardID.get16UUID(); //生成流水号
        StreamRecord streamRecord = new StreamRecord(streamRecordId,mobile,outId,inId,new Date(),money,RequestType.TRANSFER,RequestType.SUSPICIOUS);
        streamRecordDao.addRecord(streamRecord);

        streamRecord.setState(payPassword);  //将支付密码保存在state字段

        if (accountDao.getAccountMoney(outId).compareTo(money) == -1){
            return "余额不足，转账失败！";
        }
        try {
            SocketUtil util = new SocketUtil("localhost",8083);
            Response request = new Response();
            ResponseHead responseHead = new ResponseHead();
            responseHead.setType(RequestType.TRANSFER);
            request.setHead(responseHead);
            request.setBody(streamRecord);

            String xml = JaxbUtil.convertToXml(request, StreamRecord.class);//转换为xml报文

            String resXml = SocketUtil.sendXmlSocket(xml);  //接收回复的xml
            //解析报文
            Response response = JaxbUtil.convertToJavaBean(resXml,Response.class,ResponseHead.class);
            String result = response.getHead().getMessage();
            //判断状态并更新流水表
            if("".equals(result) || result == null){
                streamRecordDao.updateState(streamRecordId, RequestType.SUCCESS);
            }else {
                streamRecordDao.updateState(streamRecordId, RequestType.FAIL);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "转账失败！";
        }
        return "";
    }

    @Override
    @Transaction
    public String transferCore(String inId, String outId, String payPassword, BigDecimal money) {
        BigDecimal inAccountMoney = accountDao.getAccountMoney(inId).add(money);
        BigDecimal outAccountMoney = accountDao.getAccountMoney(outId).subtract(money);
        String password = accountDao.getAccountInfo(outId).getPayPassword();
        if(!password.equals(payPassword)){
            return "支付密码错误";
        }
            //转入
            accountDao.setMoney(inId,inAccountMoney);
//            System.out.println(1/0);
            //转出
            accountDao.setMoney(outId, outAccountMoney);
            String id = GenerateBankCardID.get16UUID();
            TransferRecord record = new TransferRecord(id, outId, inId, new Date(), money);
            transferRecordDao.addRecord(record);

            return "";
    }

    @Override
    public BigDecimal getRestMoney(String accountId){
        return accountDao.getAccountMoney(accountId);
    }

}
