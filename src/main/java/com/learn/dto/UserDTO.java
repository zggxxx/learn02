package com.learn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ApiModel("用户DTO")
public class UserDTO {
    @ApiModelProperty(value="用户ID",example="12345",required = true)
    private Long userId;
    @ApiModelProperty(value="用户名",example="zgx",required=true)
    private String userName;
    @ApiModelProperty(value="用户密码",example="19960621",required=true)
    private String password;
    @ApiModelProperty(value="电子邮件",example="12345@qq.com")
    private String email;
    @ApiModelProperty(value="手机号")
    private String mobile;
    @ApiModelProperty(value="昵称")
    private String nickname;
    @ApiModelProperty(value="角色")
    private Long[] roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long[] getRoles(){
        return roles;
    }
    public void setRoles(Long[] roles){
        this.roles = roles;
    }
}
