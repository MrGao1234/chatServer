package com.example.chatserver.dao;

import com.example.chatserver.bean.FriendsInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendsInformationDao extends JpaRepository<FriendsInformation,String> {

 /*   @Query(value = "")
    List<FriendsInformation> friendsInformationList();*/

    @Query(value = "select f from friends_information f where sender = ?1 and del_flag = 0 and pass_flag = 1")
    List<FriendsInformation> friendsList(String sender);

    @Query(value = "select f from friends_information f where sender = ?1 and del_flag = 0 and pass_flag = 1 and window_flag = 1")
    List<FriendsInformation> friendsListByOpenWindow(String sender);
}
