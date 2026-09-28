package com.learn.service;

import com.learn.dto.UserDTO;
import com.learn.entity.UserEntity;
import com.learn.vo.UserVO;

public interface LoginService {
    public UserVO getUserInfo(UserDTO user);
}
