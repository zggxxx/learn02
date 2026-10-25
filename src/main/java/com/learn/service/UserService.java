package com.learn.service;

import com.learn.dto.UserDTO;
import com.learn.entity.UserEntity;
import com.learn.vo.UserVO;

import java.util.List;

public interface UserService {
    List<UserVO> getUsers();
    UserVO getUserByUserId(Long userId);
    UserVO addUser(UserEntity user);
    void updateUser(UserDTO userDTO);
}
