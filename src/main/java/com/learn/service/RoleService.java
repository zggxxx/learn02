package com.learn.service;

import com.learn.dto.RoleDTO;
import com.learn.vo.RoleVO;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleVO addRole(RoleDTO roleDTO);
    RoleVO getRoleByRoleId(Long roleId);
    List<RoleVO> getRoles();
    Set<String> findRoleCodesByUserId(Long userId);
}
