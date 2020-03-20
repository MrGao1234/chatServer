package com.example.chatserver.component;

import com.example.chatserver.pojo.SocketServerPojo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 随项目启动启动，开启socket客户端
 * */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new SocketServerPojo().server();
    }
}
/*
public class ApplicationRunnerImpl {
   *//* @Override
    public void run(ApplicationArguments args) throws Exception {
        //new SocketServerPojo().server();
    }*//*
}*/
