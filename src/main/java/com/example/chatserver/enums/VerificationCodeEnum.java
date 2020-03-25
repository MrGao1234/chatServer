package com.example.chatserver.enums;

public enum VerificationCodeEnum {

    //短信
    phoneVerificationCode("001"),

    //图片
    photoVerificationCode("002");

    private String code;
    VerificationCodeEnum(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }



}
