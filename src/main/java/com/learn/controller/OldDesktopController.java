package com.learn.controller;

import com.learn.service.DesktopService;
import com.learn.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="旧设备开发功能push")
@RequestMapping("/old")
@RestController
public class OldDesktopController {
    @Autowired
    private DesktopService desktopService;
    @ApiOperation("获取旧设备电脑名称")
    @GetMapping("/getdesktop")
    public String getDesktopInfo(){
        return desktopService.getDesktopInfo();
    }
    @ApiOperation("获取加密后的token")
    @GetMapping("/getToken")
    public String getToken(@RequestParam("userId") Long userId,@RequestParam("username") String username){
        return JwtUtil.createToken(userId,username);
    }
}
