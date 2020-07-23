package com.cxd.wallmaker_demo;

/**
 * 接口框架实体
 * @param <T>
 */
public class Common<T>{
    private int code ; //1：成功   -1：失败
    private T content ;
    private String message ; //错误信息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Common(){

    }
}

