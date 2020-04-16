package com.example.chatserver.dao;

import com.example.chatserver.bean.MessageInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageInformationDao extends JpaRepository<MessageInformation,String> {

}
