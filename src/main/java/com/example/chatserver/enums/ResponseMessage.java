package com.example.chatserver.enums;

public enum ResponseMessage {
    SuccessPhone("验证码发送成功"),
    SuccessMessage("操作成功"),
    SuccessRegister("注册成功"),
    SuccessLogin("登录成功"),
    SuccessUpload("上传成功"),
    FailLoginAccountNotFount("该账户不存在"),
    FailLoginPasswordWrong("密码错误"),
    FailCerificode("验证码错误"),
    FailMessage("操作失败"),
    FailUpload("上传失败"),
    FailPhoneAlreadyExits("该手机号码已经注册");


    private String message;

    ResponseMessage(String message){
        this.message = message;
    }
    public String toString(){
        return this.message;
    }
}
