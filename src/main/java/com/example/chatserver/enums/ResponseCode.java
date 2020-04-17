package com.example.chatserver.enums;

public enum ResponseCode {
    SuccessMessage(200),
    FailMessage(300),
    WrongVerifation(301),
    NotFindMessage(404),

    //好友关系
    ISFRIEND(101),
    SINGLEFRIEND(102),
    NOFRIEND(103);

    private int code;

    ResponseCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
}
