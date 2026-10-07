package com.learn.controller;

import com.learn.entity.UserEntity;
import com.learn.service.TestService;
import com.learn.utils.UserContext;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Api(tags="测试模块")
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;
    @ApiOperation("测试方法一")
    @GetMapping("/do")
    public String doTest(){
        return UserContext.getUserId() + testService.doTest();
    }

    @ApiOperation("你好世界方法")
    @GetMapping("/helloworld")
    public String doHelloWorld(){
        return testService.doHelloworld();
    }
    @ApiOperation("获取用户方法")
    @ApiResponses({
            @ApiResponse(code= 200, message = "成功", response = UserEntity.class),
            @ApiResponse(code = 404, message = "用户不存在")
    })
    @GetMapping("/getUser")
    public UserEntity getUserInfo(@ApiParam(value="用户ID",required=true,example = "12345") @RequestParam("userId") Long userId){
        UserEntity user = null;
        if(Long.valueOf( 12345L).equals(userId)){
            user = new UserEntity("Lilis A","12345qwertyu",userId);
        }
        return user;
    }
    @ApiOperation("测试冲突方法")
    @GetMapping("/testConflictMerge")
    public String testConflictMerge(){
        return "New Test Conflict Merge";
    }
    @ApiOperation("你好NewB")
    @GetMapping("/helloNewB")
    public String helloNewB(){
        return "HelloNewB";
    }
    @ApiOperation("测试校验人")
    @GetMapping("/testReviewer")
    public String testReviewer(){
        return "Test Reviewer";
    }
}
