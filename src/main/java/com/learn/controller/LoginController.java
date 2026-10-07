package com.learn.controller;

import com.learn.dto.UserDTO;
import com.learn.service.LoginService;
import com.learn.vo.UserVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(tags="用户登录接口")
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @ApiOperation("登录")
    @PostMapping
    public ResponseEntity<UserVO> login(@RequestBody UserDTO loginUser, HttpServletRequest request){
        UserVO userVO = loginService.getUserInfo(loginUser);
        if(userVO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        HttpSession session =request.getSession();
        request.changeSessionId();
        session.setAttribute("LOGIN_USER",userVO);
        return ResponseEntity.ok(userVO);
    }
}
