package com.learn.controller;

import com.learn.entity.UserEntity;
import com.learn.service.TestService;
import com.learn.service.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;
    @GetMapping("/do")
    public String doTest(){
        return testService.doTest();
    }

    @GetMapping("/helloworld")
    public String doHelloWorld(){
        return testService.doHelloworld();
    }

    @GetMapping("/getUser")
    public UserEntity getUserInfo(@RequestParam("userId") Long userId){
        UserEntity user = null;
        if(userId == 12345L){
            user = new UserEntity();
            user.setUserId(userId);
            user.setUserName("Lilis A");
            user.setPassword("12345qwertyu");
        }
        return user;
    }
    @GetMapping("/testConflictMerge")
    public String testConflictMerge(){
        return "New Test Conflict Merge";
    }
    @GetMapping("/helloNewB")
    public String helloNewB(){
        return "HelloNewB";
    }
}
