package com.learn.dao;

import com.learn.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {
    List<UserEntity> queryUsers();
    UserEntity queryUserById(@Param("userId")Long userId);
    int addUser(UserEntity user);
    int deleteRoleByUserId(Long userId);
    int updateUser(UserEntity user);
}
