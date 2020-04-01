package com.example.chatserver.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String type;
    private String message;
    private String from;
    private String sendTime;
    private String to;
    private String toTime;
}
