package com.learn.controller;

import com.learn.dto.UserDTO;
import com.learn.service.LoginService;
import com.learn.service.MenuService;
import com.learn.service.RoleService;
import com.learn.service.UserService;
import com.learn.utils.JwtUtil;
import com.learn.vo.LoginResponse;
import com.learn.vo.MenuVO;
import com.learn.vo.Result;
import com.learn.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Api(tags="用户登录接口")
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;
    @ApiOperation("登录")
    @PostMapping
    public Result<LoginResponse> login(@RequestBody UserDTO loginUser){
        UserVO userVO = loginService.getUserInfo(loginUser);
        if(userVO == null){
            return Result.error("用户不存在");
        }
        LoginResponse loginResponse = new LoginResponse();
        String token = JwtUtil.createToken(userVO.getUserId(),userVO.getUserName());
        String refreshToken = JwtUtil.createRefreshToken(userVO.getUserId(),userVO.getUserName());
        redisTemplate.opsForValue().set("jwt:access:" + userVO.getUserId(),token);
        redisTemplate.opsForValue().set("jwt:refresh:" + userVO.getUserId(),refreshToken);
        List<MenuVO> menuVOs = menuService.findMenuTreeByUserId(userVO.getUserId());
        Set<String> perms = menuService.findPermsByUserId(userVO.getUserId());
        Set<String> roleCodes = roleService.findRoleCodesByUserId(userVO.getUserId());
        loginResponse.setMenus(menuVOs);
        loginResponse.setPermissions(perms);
        loginResponse.setRoleCodes(roleCodes);
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        return Result.success(loginResponse);
    }
    @PostMapping("/refresh")
    public Result refresh(@RequestHeader("Refresh-Token")String refreshToken){
        try{
             JwtUtil.validate(refreshToken);
        }catch(Exception e){
            return Result.error(401,"refresh token 无效，请重新登录");
        }
        Claims claims = JwtUtil.parseToken(refreshToken);
        Long userId = claims.get("userId",Long.class);
        String redisRefresh = redisTemplate.opsForValue().get("jwt:refresh:" + userId);
        if(!redisRefresh.equals(refreshToken)){
            return Result.error(401,"refresh token 失效");
        }

        UserVO userVO =userService.getUserByUserId(userId);
        String newAccess = JwtUtil.createToken(userVO.getUserId(),userVO.getUserName());
        redisTemplate.opsForValue().set("jwt:access:",newAccess,60, TimeUnit.MINUTES);
        return Result.success(Map.of("accessToken",newAccess));
    }

    public Result logout(){
        Long userId = (Long) SecurityUtils.getSubject().getPrincipal();
        if(userId != null){
            redisTemplate.delete("jwt:access:" + userId);
            redisTemplate.delete("jwt:refresh:" +userId );
        }
        return Result.success();
    }
}
