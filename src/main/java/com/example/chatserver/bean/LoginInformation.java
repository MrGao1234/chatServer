package com.example.chatserver.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Data
@Entity(name="login_information")
public class LoginInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String account;
    private Date date;
    private String type;

    public LoginInformation(){}
    public LoginInformation(String account,Date date,String type){
        this.account = account;
        this.date = date;
        this.type = type;
    }
}
