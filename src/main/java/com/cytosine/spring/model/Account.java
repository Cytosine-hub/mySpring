package com.cytosine.spring.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD) //表示使用这个类中的 private 非静态字段作为 XML 的序列化的属性或者元素,对应属性要使用get、set方法
@XmlRootElement
public class Account {
    @XmlElement(name="AccountId")
    private String accountId;
    @XmlElement(name="MobilePhone")
    private String mobilePhone;
    @XmlElement(name="PersonalId")
    private String personalId;//身份证号
    @XmlElement(name="Name")
    private String name;  //用户姓名
    @XmlElement(name="PayPassword")
    private String payPassword;
    @XmlElement(name="Money")
    private BigDecimal money;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
