package com.learn.controller;

import com.learn.dto.UserDTO;
import com.learn.entity.UserEntity;
import com.learn.entity.UserPrincipal;
import com.learn.service.LoginService;
import com.learn.service.UserService;
import com.learn.vo.Result;
import com.learn.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @GetMapping("/current")
    public Result<UserVO> getCurrentUserInfo(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityUtils.getSubject().getPrincipal();
        Long currentUserId = userPrincipal.getId();
        UserVO userVO = loginService.getCurrentUserInfo(currentUserId);
        if(userVO == null){
            return Result.error("登录已失效");
        }
        return Result.success(userVO);
    }
    @RequiresPermissions("user:list")
    @GetMapping("/all")
    public Result<List<UserVO>> getUsers(){
        List<UserVO> userVOS = userService.getUsers();
        return Result.success(userVOS);
    }
    @GetMapping("/query")
    public Result<UserVO> getUserById(@RequestParam("userId") Long userId){
        return Result.success(userService.getUserByUserId(userId));
    }

    @PostMapping("/add")
    public Result<UserVO> addUser(@RequestBody UserDTO userDTO)
    {
        UserEntity user= new UserEntity(userDTO.getUserName(),userDTO.getPassword(), 0L);
        user.setEmail(userDTO.getEmail());
        user.setMobile(userDTO.getMobile());
        user.setNickname(userDTO.getNickname());
        Long currentUserId = ((UserPrincipal)SecurityUtils.getSubject().getPrincipal()).getId();
        user.setCreateBy(currentUserId);
        user.setUpdateBy(currentUserId);
        UserVO userVO = userService.addUser(user);
        return Result.success(userVO);
    }
    @RequiresPermissions("user:editor")
    @PostMapping("/update")
    public Result updateUser(@RequestBody UserDTO userDTO){
        userService.updateUser(userDTO);
        return Result.success();
    }
}
