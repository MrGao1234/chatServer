package com.example.chatserver.service;

import com.alibaba.fastjson.JSON;
import com.example.chatserver.bean.FriendsInformation;
import com.example.chatserver.bean.MessageInformation;
import com.example.chatserver.dao.FriendsInformationDao;
import com.example.chatserver.dao.MessageInformationDao;
import com.example.chatserver.dao.UserDao;
import com.example.chatserver.enums.ResponseCode;
import com.example.chatserver.enums.ResponseMessage;
import com.example.chatserver.pojo.ResponsePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsService {

    @Autowired
    private FriendsInformationDao friendsInformationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageInformationDao messageInformationDao;

    //friendList
    public String getFriendList(){

        String name = "zhang";
        List<FriendsInformation> friendList = setList(friendsInformationDao.friendsList(name));
        return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(), ResponseMessage.SuccessMessage.toString(), JSON.toJSONString(friendList));
    }

    //messageList
    public String getFriendMessageList(){
        String name = "zhang";
        List<FriendsInformation> friendList = setList(friendsInformationDao.friendsList(name));
        for(int i = 0;i < friendList.size();i++){
            List<MessageInformation> messageInformationList = messageInformationDao.getMessageList(friendList.get(i).getAccepter());
            friendList.get(i).setMessageList(messageInformationList);
        }
        return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(), ResponseMessage.SuccessMessage.toString(), JSON.toJSONString(friendList));
    }

    public List<FriendsInformation> setList(List<FriendsInformation> friendList){
        //friendMessage
        for(int i = 0;i < friendList.size();i++){
            friendList.get(i).setUser( userDao.findFirstByAccount(friendList.get(i).getAccepter()) );
        }
        return friendList;
    }
}
