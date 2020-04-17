package com.example.chatserver.enums;

public enum SocketEnum {

    KEY_TYPE("type"),
    KEY_CLIENTNAME("clientName"),
    KEY_MESSAGE("message"),
    KEY_TO("to"),


    VALUE_OPEN("OPEN"),
    VALUE_CLOSE("CLOSE"),
    VALUE_MESSAGE("MESSAGE"),
    VALUE_ADDREQUEST("ADDREQUEST"),
    VALUE_AGREE("AGREE"),
    VALUE_REFUSE("REFUSE");

    private String type;
    SocketEnum(String type){
        this.type = type;
    }
}
