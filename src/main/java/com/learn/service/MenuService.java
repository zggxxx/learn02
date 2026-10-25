package com.learn.service;

import com.learn.dto.MenuDTO;
import com.learn.entity.MenuEntity;
import com.learn.vo.MenuVO;

import java.util.List;
import java.util.Set;

public interface MenuService {
    MenuVO addMenu(MenuDTO menuDTO);
    MenuVO getMenuByMenuId(Long menuId);
    List<MenuVO> getMenus();
    List<MenuVO> findMenuTreeByUserId(Long userId);
    Set<String> findPermsByUserId(Long userId);
}
