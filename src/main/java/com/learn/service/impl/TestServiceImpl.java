package com.learn.service.impl;

import com.learn.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String doTest() {
        return "Hello World!";
    }
    @Override
    public String doHelloworld(){
        return "Feature/helloworld";
    }
}
