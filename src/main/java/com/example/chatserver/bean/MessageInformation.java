package com.example.chatserver.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "message_information")
public class MessageInformation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String message;
    private Date time;
    private String sender;
    private String accepter;
    private String status;

    public MessageInformation(){}
    public MessageInformation(String message,Date time,String sender,String accepter,String status){
        this.status = status;
        this.message = message;
        this.sender = sender;
        this.accepter = accepter;
        this.time = time;
    }
}
