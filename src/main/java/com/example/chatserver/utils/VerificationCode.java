package com.example.chatserver.utils;
/**
 * 生成验证码
 * */
public class VerificationCode {

    /*短信验证码*/
    public static String phoneVerificationCode(){
        String code = "";
        for(int i = 0;i < 6;i++){
            code += (int)(Math.random() * 10);
        }
        return code;
    }

}
