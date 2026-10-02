package com.learn.controller;

import com.learn.service.DesktopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
