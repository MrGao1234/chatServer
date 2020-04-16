package com.example.chatserver.dao;

import com.example.chatserver.bean.LoginInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInformationDao extends JpaRepository<LoginInformation,String> {
}
