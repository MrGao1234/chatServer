package com.example.chatserver.enums;

public enum LoginInformationEnum {

    //登录
    LOGIN("1"),

    //退出
    EXIT("2"),

    //注册
    REGISTER("3");

    private String code;
    LoginInformationEnum(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }



}
