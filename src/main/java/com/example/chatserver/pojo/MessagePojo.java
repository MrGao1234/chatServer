package com.example.chatserver.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class MessagePojo {
    private String type;
    private String message;
    private String from;
    private String to;
    private String time;

    public MessagePojo(){}
    public MessagePojo(String type,String message,String from,String to,String time){
        this.type = type;
        this.message = message;
        this.from = from;
        this.to = to;
        this.time = time;
    }
}
