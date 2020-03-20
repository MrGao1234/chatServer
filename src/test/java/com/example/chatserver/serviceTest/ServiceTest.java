package com.example.chatserver.serviceTest;

import com.example.chatserver.service.RegisterAndLoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private RegisterAndLoginService registerAndLoginService;

    @Test
    public void phoneTest(){
        registerAndLoginService.sendPhoneVerificationCode("18149781319");
    }

}
