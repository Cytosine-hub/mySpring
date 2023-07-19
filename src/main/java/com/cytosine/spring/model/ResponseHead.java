package com.cytosine.spring.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * 服务端返回数据
 */


@XmlAccessorType(XmlAccessType.FIELD) //表示使用这个类中的 private 非静态字段作为 XML 的序列化的属性或者元素,对应属性要使用get、set方法
@XmlRootElement
public class ResponseHead {
    @XmlElement(name="Message",required=true)
    private String message;  //消息
    @XmlElement(name="Code",required=true)
    private String code;  //状态码
    @XmlElement(name = "Type",required = true)
    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
