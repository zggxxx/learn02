package com.learn.controller;

import com.learn.dto.UserDTO;
import com.learn.service.LoginService;
import com.learn.utils.JwtUtil;
import com.learn.vo.Result;
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
    public Result<String> login(@RequestBody UserDTO loginUser, HttpServletRequest request){
        UserVO userVO = loginService.getUserInfo(loginUser);
        if(userVO == null){
            return Result.error("用户不存在");
        }
        HttpSession session =request.getSession();
        String token = JwtUtil.createToken(userVO.getUserId(),userVO.getUserName());
        request.changeSessionId();
        session.setAttribute("LOGIN_USER",userVO);
        return Result.success(token);
    }
}
