package com.learn.controller;

import com.learn.service.LoginService;
import com.learn.utils.UserContext;
import com.learn.vo.Result;
import com.learn.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private LoginService loginService;
    @GetMapping("/current")
    public Result<UserVO> getCurrentUserInfo(){
        Long currentUserId = UserContext.getUserId();
        UserVO userVO = loginService.getCurrentUserInfo(currentUserId);
        if(userVO == null){
            return Result.error("登录已失效");
        }
        return Result.success(userVO);
    }
}
