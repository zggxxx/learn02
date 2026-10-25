package com.learn.service.impl;

import com.learn.dao.RoleDAO;
import com.learn.dto.RoleDTO;
import com.learn.entity.RoleEntity;
import com.learn.service.RoleService;
import com.learn.vo.RoleVO;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDAO roleDAO;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleVO addRole(RoleDTO roleDTO) {
        RoleEntity role = new RoleEntity();
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleCode(roleDTO.getRoleCode());
        role.setRemark(roleDTO.getRemark());
        Long currentUserId = (Long) SecurityUtils.getSubject().getPrincipal();
        role.setCreateBy(currentUserId);
        role.setUpdateBy(currentUserId);
        int rows = roleDAO.addRole(role);
        if(rows<1){
            throw new SqlSessionException("添加角色失败");
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleId(role.getRoleId());
        roleVO.setRoleName(role.getRoleName());
        roleVO.setRoleCode(role.getRoleCode());
        roleVO.setRemark(role.getRemark());
        return roleVO;
    }

    @Override
    public RoleVO getRoleByRoleId(Long roleId) {
        RoleEntity roleEntity = roleDAO.queryRoleByRoleId(roleId);
        if(roleEntity == null){
            throw new SqlSessionException("用户不存在");
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleId(roleEntity.getRoleId());
        roleVO.setRoleName(roleEntity.getRoleName());
        roleVO.setRoleCode(roleEntity.getRoleCode());
        roleVO.setRemark(roleEntity.getRemark());
        return roleVO;
    }

    @Override
    public List<RoleVO> getRoles() {
        List<RoleVO> roleVOS = new ArrayList<>();
        List<RoleEntity> roles = roleDAO.queryRoles();
        for(RoleEntity role : roles){
            RoleVO roleVO = new RoleVO();
            roleVO.setRoleName(role.getRoleName());
            roleVO.setRoleCode(role.getRoleCode());
            roleVO.setRemark(role.getRemark());
            roleVO.setRoleId(role.getRoleId());
            roleVOS.add(roleVO);
        }
        return roleVOS;
    }
    @Override
    public Set<String> findRoleCodesByUserId(Long userId){
        Set<String> roleCodes = roleDAO.findRoleCodesByUserId(userId);
        return roleCodes == null ? Collections.emptySet() : roleCodes;
    }
}
