package com.learn.service.impl;

import com.learn.dao.UserDAO;
import com.learn.dto.UserDTO;
import com.learn.entity.UserEntity;
import com.learn.service.LoginService;
import com.learn.vo.UserVO;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserDAO userDAO;
    @Override
    public UserVO getUserInfo(UserDTO user) {
        UserVO userVO = null;
        UserEntity userEntity = userDAO.queryUserById(user.getUserId());
        if(userEntity == null){
            throw new SqlSessionException("用户不存在");
        }
        if(!userEntity.getPassword().equals(user.getPassword())){
            throw new RuntimeException("密码不正确");
        }
        userVO = new UserVO(userEntity.getUserName(),user.getUserId(),userEntity.getNickname());
        return userVO;
    }

    @Override
    public UserVO getCurrentUserInfo(Long userId){
        UserEntity userEntity = userDAO.queryUserById(userId);
        if(userEntity == null){
            throw new SqlSessionException("用户不存在");
        }
        UserVO userVO = new UserVO(userEntity.getUserName(),userEntity.getUserId(),userEntity.getNickname());
        return userVO;
    }
}
