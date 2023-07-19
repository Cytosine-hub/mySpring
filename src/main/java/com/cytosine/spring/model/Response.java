package com.cytosine.spring.model;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD) //表示使用这个类中的 private 非静态字段作为 XML 的序列化的属性或者元素,对应属性要使用get、set方法
@XmlRootElement(name = "SOCKET")
@XmlType(propOrder = {"head","body"})
public class Response<T> {
    @XmlElement(name = "HEAD",required = true)
    private ResponseHead head;
    @XmlElement(name = "BODY",required = true)
    private T body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
