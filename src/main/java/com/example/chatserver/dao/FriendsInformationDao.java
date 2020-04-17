package com.example.chatserver.dao;

import com.example.chatserver.bean.FriendsInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendsInformationDao extends JpaRepository<FriendsInformation,String> {

    @Query(value = "select f from friends_information f where sender = ?1 and del_flag = 0 and pass_flag = 1")
    List<FriendsInformation> friendsList(String sender);

    @Query(value = "select f from friends_information f where sender = ?1 and del_flag = 0 and pass_flag = 1 and window_flag = 1")
    List<FriendsInformation> friendsListByOpenWindow(String sender);


    @Query(value = "select f from friends_information f where sender = ?1 and accepter = ?2 and del_flag = 0 and pass_flag = 1")
    FriendsInformation findSenderFrientByAccepter(String sender,String accepter);

    @Query(value = "select f from friends_information f where sender = ?2 and accepter = ?1 and del_flag = 0 and pass_flag = 1")
    FriendsInformation findAccepterFriendBySender(String sender,String accepter);

//    @Transactional
//    @Modifying
//    @Query(value = "update FriendsInformation set window_flag = 1,passFlag = 1 where sender = :sender and accepter = :accepter")
//    int replyAddRequest(String sender, String accepter);
}
