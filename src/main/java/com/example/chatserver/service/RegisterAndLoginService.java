package com.example.chatserver.service;

import com.aliyuncs.exceptions.ClientException;
import com.example.chatserver.bean.User;
import com.example.chatserver.dao.UserDao;
import com.example.chatserver.enums.RedisEnum;
import com.example.chatserver.enums.ResponseCode;
import com.example.chatserver.enums.ResponseMessage;
import com.example.chatserver.enums.VerificationCodeEnum;
import com.example.chatserver.pojo.ResponsePojo;
import com.example.chatserver.pojo.UserPojo;
import com.example.chatserver.utils.RedisUtils;
import com.example.chatserver.utils.SmsUtil;
import com.example.chatserver.utils.VerificationCode;
import com.example.chatserver.utils.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

    /*发送短信*/
    public void sendPhoneVerificationCode(String phone){

        /*获取验证码*/
        String code = VerificationCode.phoneVerificationCode();

        /*存储验证码*/
        valueOperations.set(phone + VerificationCodeEnum.phoneVerificationCode.getCode(),code);
        redisUtils.expireKey(phone + VerificationCodeEnum.phoneVerificationCode.getCode(),5, TimeUnit.MINUTES);
        //System.out.println(valueOperations.get(phone));

        /*发送短信*/
        try {
            SmsUtil.sendMessage(phone,code);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /*注册用户*/
    public String registerUser(UserPojo userPojo){
        String phone = userPojo.getPhone();
        //1，判断验证码
        if( phone == null || userPojo.getVerificode() == null || !userPojo.getVerificode().equals((valueOperations.get(phone + VerificationCodeEnum.phoneVerificationCode.getCode())))){
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
        String account = (userDao.getMaxId() + 100000) + "";
        userDao.save( new User(account, DigestUtils.md5DigestAsHex(userPojo.getPassword().getBytes()),phone,userPojo.getNickName()));
        return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(),ResponseMessage.SuccessRegister.toString(),account);
    }

    /*登录*/
    public String login(UserPojo userPojo){

        /*号码或账号登录*/
        String account = userPojo.getAccount();

        //先判断号码
        List<User> userList;
        userList = userDao.findUserByAccountAndPhone(account);
        if(userList != null && userList.size() > 0){
            //说明该号码存在
            if(userList.get(0).getPassword().equals(DigestUtils.md5DigestAsHex(userPojo.getPassword().getBytes()))){

                //说明密码正确,登录成功，将该用户信息放入redis中
                hashOperations.put(RedisEnum.AlreadyLoginList.getCode(),userList.get(0).getAccount(),userList.get(0));
                redisUtils.persistKey(RedisEnum.AlreadyLoginList.getCode());

                return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(), ResponseMessage.SuccessLogin.toString());
            }else{
                return new ResponsePojo().response(ResponseCode.FailMessage.getCode(),ResponseMessage.FailLoginPasswordWrong.toString());
            }
        }
        return new ResponsePojo().response(ResponseCode.NotFindMessage.getCode(),ResponseMessage.FailLoginAccountNotFount.toString());
    }


    /*图片验证码*/
    public void drawPhoneVerificationCode(String account,HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        //禁止图像缓存
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        VerificationCodeUtil imageUtil = new VerificationCodeUtil();

        //获取验证码
        String code = imageUtil.getCode();
        valueOperations.set(account + VerificationCodeEnum.photoVerificationCode.getCode(),code);
        redisUtils.expireKey(account + VerificationCodeEnum.photoVerificationCode.getCode(),3, TimeUnit.MINUTES);

        //响应验证码图片
        imageUtil.write(response.getOutputStream());
    }
}
