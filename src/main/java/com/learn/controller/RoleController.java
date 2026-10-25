package com.learn.controller;

import com.learn.dto.RoleDTO;
import com.learn.service.RoleService;
import com.learn.vo.Result;
import com.learn.vo.RoleVO;
import com.learn.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/role")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;
    @ApiOperation("新增用户")
    @PostMapping("/add")
    public Result<Void> addRole(@RequestBody RoleDTO roleDTO){
        RoleVO roleVO = roleService.addRole(roleDTO);
        return Result.success();
    }
    @RequiresRoles("admin")
    @ApiOperation("获取用户列表")
    @GetMapping("/all")
    public Result<List<RoleVO>> getRoles(){
        return Result.success(roleService.getRoles());
    }
    @ApiOperation("根据id查询角色")
    @GetMapping("/query")
    public Result<RoleVO> getRoleByRoleId(@RequestParam("roleId") Long roleId){
        return Result.success(roleService.getRoleByRoleId(roleId));
    }
    @ApiOperation("根据用户id查询角色code")
    @GetMapping("/queryRoleCodesByUserId")
    public Result<Set<String>> getRoleCodesByUserId(@RequestParam("userId")Long userId){
        return Result.success(roleService.findRoleCodesByUserId(userId));
    }
}
