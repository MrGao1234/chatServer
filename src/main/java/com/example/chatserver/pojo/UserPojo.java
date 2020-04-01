package com.example.chatserver.pojo;

import lombok.Data;

/**用户信息pojo*/
@Data
public class UserPojo {

    private String userKey;
    private String nickName;
    private String password;
    private String phone;
    private String verificode;
    private String account;

}
