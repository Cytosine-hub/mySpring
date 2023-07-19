package com.cytosine.spring.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;


@XmlAccessorType(XmlAccessType.FIELD) //表示使用这个类中的 private 非静态字段作为 XML 的序列化的属性或者元素,对应属性要使用get、set方法
@XmlRootElement
public class StreamRecord {

    @XmlElement(name="Id")
    private String id;
    @XmlElement(name = "OptionId")
    private String optionId;
    @XmlElement(name="PayAccountId")
    private String payAccountId;
    @XmlElement(name="InAccountId")
    private String inAccountId;
    @XmlElement(name="TransTime")
    private Date transTime;
    @XmlElement(name = "Money")
    private BigDecimal transMoney;
    @XmlElement(name = "Type")
    private String type;   //收入还是支出 - 0001 - 0002
    @XmlElement(name = "State")
    private String state;


    public StreamRecord() {
    }

    public StreamRecord(String id, String optionId, String payAccountId, String inAccountId, Date transTime, BigDecimal transMoney, String type, String state) {
        this.id = id;
        this.optionId = optionId;
        this.payAccountId = payAccountId;
        this.inAccountId = inAccountId;
        this.transTime = transTime;
        this.transMoney = transMoney;
        this.type = type;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    public void setPayAccountId(String payAccountId) {
        this.payAccountId = payAccountId;
    }

    public String getInAccountId() {
        return inAccountId;
    }

    public void setInAccountId(String inAccountId) {
        this.inAccountId = inAccountId;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public BigDecimal getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(BigDecimal transMoney) {
        this.transMoney = transMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
