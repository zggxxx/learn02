package com.learn.service.impl;

import com.learn.dto.UserDTO;
import com.learn.entity.UserEntity;
import com.learn.service.LoginService;
import com.learn.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public UserVO getUserInfo(UserDTO user) {
        UserVO userVO = null;
        if(user.getUserId()== 12345L && "19960621".equals(user.getPassword())){
            UserEntity userEntity = new UserEntity("zgx",user.getPassword(),user.getUserId());
            userVO = new UserVO(userEntity.getUserName(),user.getUserId());
        }
        return userVO;
    }

    @Override
    public UserVO getCurrentUserInfo(Long userId){
        UserVO userVO = null;
        if(Long.valueOf(12345L).equals(userId)){
            userVO = new UserVO("zgx",12345L);
        }
        return userVO;
    }
}
