package com.example.chatserver.controller;

import com.example.chatserver.enums.ResponseCode;
import com.example.chatserver.enums.ResponseMessage;
import com.example.chatserver.pojo.ResponsePojo;
import com.example.chatserver.pojo.UserPojo;
import com.example.chatserver.service.RegisterAndLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(("/registerAndLogin"))
public class RegisterAndLoginController {

    @Autowired
    private RegisterAndLoginService registerAndLoginService;

    /**发送短信验证码*/
    @RequestMapping("/phoneVerificationCode")
    public String sendPhoneVerificationCode(String phone){
        registerAndLoginService.sendPhoneVerificationCode(phone);
        return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(), ResponseMessage.SuccessPhone.toString());
    }


    /**注册操作*/
    @RequestMapping("/register")
    public String register(@RequestBody UserPojo userPojo){
        return registerAndLoginService.registerUser(userPojo);
    }

    /**登录操作*/
    @RequestMapping("/login")
    public String login(@RequestBody UserPojo userPojo){
        return registerAndLoginService.login(userPojo);
    }

    /**获取图片验证码*/
    @RequestMapping("/imageVerifyCode")
    public void getVerificationCode(HttpServletResponse response) throws IOException {
       registerAndLoginService.drawPhoneVerificationCode(response);
    }
}
