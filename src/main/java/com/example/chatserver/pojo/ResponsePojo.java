package com.example.chatserver.pojo;

import com.alibaba.fastjson.JSON;

/**响应信息*/
public class ResponsePojo<T> {

    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public T getData() {
        return data;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setData(T data) {
        this.data = data;
    }
    public ResponsePojo(){}

    public String response(int code,String message){
        this.code = code;
        this.message = message;
        return JSON.toJSONString(this);
    }

    public String response(int code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
        return JSON.toJSONString(this);
    }

}
