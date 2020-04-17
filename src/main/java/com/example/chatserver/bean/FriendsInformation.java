package com.example.chatserver.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "friends_information")
public class FriendsInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sender;
    private String accepter;
    private Date time;
    private boolean delFlag;
    private boolean windowFlag;
    private boolean passFlag;
    private String componts;
    private String group;

    //friendMessage
    @Transient
    private User user;

    //messageList
    @Transient
    private List<MessageInformation> messageList;

    public FriendsInformation(){}
    public FriendsInformation(String sender,String accepter,Date time,boolean delFlag,boolean windowFlag,boolean passFlag){
        this.sender = sender;
        this.accepter = accepter;
        this.time = time;
        this.delFlag = delFlag;
        this.windowFlag = windowFlag;
        this.passFlag = passFlag;
    }
    public FriendsInformation(String sender,String accepter,Date time){
        this.sender = sender;
        this.accepter = accepter;
        this.time = time;
    }
    public FriendsInformation(String sender,String accepter,Date time,String componts,boolean passFlag){
        this.sender = sender;
        this.accepter = accepter;
        this.time = time;
        this.componts = componts;
        this.passFlag = passFlag;
    }
}
