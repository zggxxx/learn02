package com.learn.dao;

import com.learn.entity.UserRoleEntity;
import com.learn.entity.RoleEntity;

import java.util.List;
import java.util.Set;

public interface RoleDAO {
    int addRole(RoleEntity role);
    List<RoleEntity>queryRoles();
    RoleEntity queryRoleByRoleId(Long roleId);
    Set<String> findRoleCodesByUserId(Long userId);
    int deleteRolesByUserId(Long userId);
    int batchUserRole(List<UserRoleEntity> userRoleEntities);
}
