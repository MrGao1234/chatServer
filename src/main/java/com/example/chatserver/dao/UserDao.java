package com.example.chatserver.dao;

import com.example.chatserver.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,String> {

    @Query(value = "select u from user u where phone = ?1")
    List<User> findUserByPhone(String phone);

    @Query(value = "select u from user u where phone = ?1 or account = ?1")
    List<User> findUserByAccountAndPhone(String account);

    @Query(value = "select coalesce(max(id),0) from user")
    int getMaxId();
}