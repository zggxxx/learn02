package com.learn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户DTO")
public class UserDTO {
    @ApiModelProperty(value="用户ID",example="12345",required = true)
    private Long userId;
    @ApiModelProperty(value="用户密码",example="19960621",required=true)
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
