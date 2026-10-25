package com.learn.service.impl;

import com.learn.dao.RoleDAO;
import com.learn.dao.UserDAO;
import com.learn.dto.UserDTO;
import com.learn.entity.UserPrincipal;
import com.learn.entity.UserRoleEntity;
import com.learn.entity.UserEntity;
import com.learn.service.UserService;
import com.learn.vo.UserVO;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.shiro.SecurityUtils;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private RoleDAO roleDAO;
    @Override
    public List<UserVO> getUsers() {
        List<UserVO> userVOS = new ArrayList<>();
        List<UserEntity> users =  userDAO.queryUsers();
        for (UserEntity user : users) {
            UserVO userVO = new UserVO(user.getUserName(),user.getUserId(),user.getNickname());
            userVOS.add(userVO);
        }
        return userVOS;
    }

    @Override
    public UserVO getUserByUserId(Long userId) {
        UserEntity user = userDAO.queryUserById(userId);
        if(user == null){
            throw new SqlSessionException("用户不存在");
        }
        UserVO userVO = new UserVO(user.getUserName(),user.getUserId(),user.getNickname());
        return userVO;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserVO addUser(UserEntity user) {
        int rows = userDAO.addUser(user);
        if(rows < 1){
            throw new SqlSessionException("添加用户失败");
        }
        return new UserVO(user.getUserName(),user.getUserId(),user.getNickname());
    }
    @Transactional
    @Override
    public void updateUser(UserDTO userDTO) {
        roleDAO.deleteRolesByUserId(userDTO.getUserId());
        insertUserRole(userDTO);
        UserEntity user = new UserEntity();
        Long currentUserId =((UserPrincipal) SecurityUtils.getSubject().getPrincipal()).getId();
        user.setUpdateBy(currentUserId);
        BeanUtils.copyProperties(userDTO,user);
        userDAO.updateUser(user);
    }
    public void insertUserRole(UserDTO userDTO){
        this.insertUserRole(userDTO.getUserId(), userDTO.getRoles());
    }
    public void insertUserRole(Long userId,Long[] roleIds){
        List<UserRoleEntity> userRoleEntities = new ArrayList<>(roleIds.length);
        for(Long roleId : roleIds){
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userId);
            userRoleEntity.setRoleId(roleId);
            userRoleEntities.add(userRoleEntity);
        }
        roleDAO.batchUserRole(userRoleEntities);
    }
}
