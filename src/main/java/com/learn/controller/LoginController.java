package com.learn.controller;

import com.learn.dto.UserDTO;
import com.learn.service.LoginService;
import com.learn.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @PostMapping
    public ResponseEntity<UserVO> login(@RequestBody UserDTO loginUser){
        UserVO userVO = loginService.getUserInfo(loginUser);
        if(userVO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(userVO);
    }
}
