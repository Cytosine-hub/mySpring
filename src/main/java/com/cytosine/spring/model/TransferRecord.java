package com.cytosine.spring.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD) //表示使用这个类中的 private 非静态字段作为 XML 的序列化的属性或者元素,对应属性要使用get、set方法
@XmlRootElement
public class TransferRecord {
    @XmlElement(name="Id")
    private String id;
    @XmlElement(name="PayAccountId")
    private String payAccountId;
    @XmlElement(name="InAccountId")
    private String inAccountId;
    @XmlElement(name="TransTime")
    private Date transTime;
    @XmlElement(name = "Money")
    private BigDecimal money;
    @XmlElement(name = "type")
    private String type;   //收入还是支出


    public TransferRecord() {
    }

    public TransferRecord(String id, String payAccountId, String inAccountId, Date transTime, BigDecimal money) {
        this.id = id;
        this.payAccountId = payAccountId;
        this.inAccountId = inAccountId;
        this.transTime = transTime;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
