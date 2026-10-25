package com.learn.controller;

import com.learn.dto.MenuDTO;
import com.learn.entity.MenuEntity;
import com.learn.service.MenuService;
import com.learn.vo.MenuVO;
import com.learn.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/menu")
@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;
    @ApiOperation("查询所有菜单")
    @GetMapping("/all")
    public Result<List<MenuVO>> getMenus(){
        List<MenuVO> menuVOS = menuService.getMenus();
        return Result.success(menuVOS);
    }
    @ApiOperation("添加菜单")
    @PostMapping("/add")
    public Result<Void> addMenu(@RequestBody MenuDTO menuDTO){
        MenuVO menuVO = menuService.addMenu(menuDTO);
        return Result.success();
    }
    @ApiOperation("根据id查询菜单")
    @GetMapping("/query")
    public Result<MenuVO> getMenuByMenuId(@RequestParam("menuId") Long menuId){
        MenuVO menuVO = menuService.getMenuByMenuId(menuId);
        return Result.success(menuVO);
    }
    @ApiOperation("获取用户菜单")
    @GetMapping("/{id}")
    public Result<List<MenuVO>> getMenusTreeByUserId(@PathVariable("id")Long userId){
        return Result.success(menuService.findMenuTreeByUserId(userId));
    }
    @ApiOperation("查询用户权限")
    @GetMapping("/queryPermsByUserId")
    public Result<Set<String>> getPermsByUserId(@RequestParam("userId")Long userId){
        return Result.success(menuService.findPermsByUserId(userId));
    }
}
