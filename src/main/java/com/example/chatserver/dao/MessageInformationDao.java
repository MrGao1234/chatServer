package com.example.chatserver.dao;

import com.example.chatserver.bean.MessageInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageInformationDao extends JpaRepository<MessageInformation,String> {

    @Query(nativeQuery=true,value = "select * from message_information m where (sender = ?1 or accepter = ?1) order by time desc limit 30")
    List<MessageInformation> getMessageList(String name);

}
