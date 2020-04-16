package com.example.chatserver.service;

import com.aliyuncs.exceptions.ClientException;
import com.example.chatserver.bean.LoginInformation;
import com.example.chatserver.bean.User;
import com.example.chatserver.dao.LoginInformationDao;
import com.example.chatserver.dao.UserDao;
import com.example.chatserver.enums.LoginInformationEnum;
import com.example.chatserver.enums.RedisEnum;
import com.example.chatserver.enums.ResponseCode;
import com.example.chatserver.enums.ResponseMessage;
import com.example.chatserver.pojo.ResponsePojo;
import com.example.chatserver.pojo.UserPojo;
import com.example.chatserver.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RegisterAndLoginService {

    @Autowired
    private ValueOperations valueOperations;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HashOperations hashOperations;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginInformationDao loginInformationDao;

    /*发送短信*/
    public void sendPhoneVerificationCode(String phone){

        /*获取验证码*/
        String code = VerificationCode.phoneVerificationCode();

        /*存储验证码*/
        valueOperations.set(SessionUtils.getSession().getId() + phone,code);
        redisUtils.expireKey(SessionUtils.getSession().getId() + phone,5, TimeUnit.MINUTES);
        //System.out.println(valueOperations.get(phone));

        /*发送短信*/
        try {
            SmsUtil.sendMessage(phone,code);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    /*注册用户*/
    public String registerUser(UserPojo userPojo){
        String phone = userPojo.getPhone();
        //1，判断验证码
        if( phone == null || userPojo.getVerificode() == null || !userPojo.getVerificode().equals((valueOperations.get( SessionUtils.getSession().getId() + phone )))){
            return new ResponsePojo().response(ResponseCode.FailMessage.getCode(), ResponseMessage.FailCerificode.toString());
        }
        //2，判断号码是否存在
        List<User> userListByPhone = userDao.findUserByPhone(phone);
        if(userListByPhone != null && userListByPhone.size() > 0)
            return new ResponsePojo().response(ResponseCode.FailMessage.getCode(),ResponseMessage.FailPhoneAlreadyExits.toString());
        /*
         3,完成注册
            3.1 账号默认生成，作为唯一不能重复
         */
        String account = UUID.randomUUID().toString().replace("-","");
        userDao.save( new User(account, DigestUtils.md5DigestAsHex(userPojo.getPassword().getBytes()),phone,userPojo.getNickName()));
        loginInformationDao.save(new LoginInformation(account,new Date(), LoginInformationEnum.REGISTER.getCode()));
        return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(),ResponseMessage.SuccessRegister.toString());
    }

    /*登录*/
    public String login(UserPojo userPojo){

        /*号码或账号登录*/
        String account = userPojo.getAccount();

        //先判断号码
        List<User> userList;
        userList = userDao.findUserByPhone(account);
        if(userList != null && userList.size() > 0){

            //判断验证码是否正确
            String sessionId = SessionUtils.getSession().getId();
            if( userPojo.getVerificode() == null || !userPojo.getVerificode().equals(valueOperations.get(sessionId))){
                return new ResponsePojo().response(ResponseCode.WrongVerifation.getCode(),ResponseMessage.FailCerificode.toString());
            }

            //说明该号码存在
            if(userList.get(0).getPassword().equals(DigestUtils.md5DigestAsHex(userPojo.getPassword().getBytes()))){

                //说明密码正确,登录成功，根据 sessionId 将该用户信息放入redis中
                //String userKey = UUID.randomUUID().toString();
                hashOperations.put(RedisEnum.AlreadyLoginList.getCode(),SessionUtils.getSession().getId(),userList.get(0));
                redisUtils.persistKey(RedisEnum.AlreadyLoginList.getCode());

                return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(), ResponseMessage.SuccessLogin.toString(),userList.get(0));
            }else{
                return new ResponsePojo().response(ResponseCode.FailMessage.getCode(),ResponseMessage.FailLoginPasswordWrong.toString());
            }
        }
        return new ResponsePojo().response(ResponseCode.NotFindMessage.getCode(),ResponseMessage.FailLoginAccountNotFount.toString());
    }


    /*图片验证码*/
    public void drawPhoneVerificationCode(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        //禁止图像缓存
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        VerificationCodeUtil imageUtil = new VerificationCodeUtil();

        //获取验证码
        String code = imageUtil.getCode();
        String sessionId = SessionUtils.getSession().getId();
        valueOperations.set(sessionId,code);
        redisUtils.expireKey(sessionId,3, TimeUnit.MINUTES);

        //响应验证码图片
        imageUtil.write( response.getOutputStream() );
    }

    //当登录成功后，将好友列表和聊天列表都返回，用于初始化页面

}
