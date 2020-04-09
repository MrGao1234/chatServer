package com.example.chatserver.enums;

public enum ResponseCode {
    SuccessMessage(200),
    FailMessage(300),
    WrongVerifation(301),
    NotFindMessage(404);

    private int code;

    ResponseCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
}
