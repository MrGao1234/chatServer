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

    //friendMessage
    @Transient
    private User user;

    //messageList
    @Transient
    private List<MessageInformation> messageList;
}
