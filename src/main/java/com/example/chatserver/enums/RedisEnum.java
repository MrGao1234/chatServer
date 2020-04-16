package com.example.chatserver.enums;

public enum RedisEnum {

    //已登录用户列表
    AlreadyLoginList("alreadyLoginList"),

    //聊天消息列表
    ChatMessageList("chatMessageList");

    private String code;
    RedisEnum(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }

}
