package com.learn.dao;

import com.learn.entity.MenuEntity;

import java.util.List;
import java.util.Set;

public interface MenuDAO {
    int addMenu(MenuEntity menuEntity);
    List<MenuEntity> queryMenus();
    MenuEntity queryMenuByMenuId(Long menuId);

    List<MenuEntity> findMenusByUserId(Long userId);
    Set<String> findPermsByUserId(Long userId);
}
