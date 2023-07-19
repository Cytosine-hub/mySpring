package com.cytosine.spring.mvc.entity;

/**
 * controller返回结果对象
 */

public class Result {

    public static final int SUCCESS_CODE = 200;
    public static final int FAILED_CODE = 500;

    private int code;  //状态码
    private String message; //携带信息
    private Object data; //数据

    public Result(){

    }
    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
