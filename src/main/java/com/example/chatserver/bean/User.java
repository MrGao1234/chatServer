package com.example.chatserver.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String account;
    private String password;
    private String phone;
    private String nickName;

    public User(){}
    public User(String account,String password,String phone,String nickName){
        this.account = account;
        this.password = password;
        this.phone = phone;
        this.nickName = nickName;
    }
    public User(int id,String account,String password,String phone,String nickName){
        this.id = id;
        this.account = account;
        this.password = password;
        this.phone = phone;
        this.nickName = nickName;
    }
}
