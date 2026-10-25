package com.learn.service.impl;

import com.learn.dao.MenuDAO;
import com.learn.dto.MenuDTO;
import com.learn.entity.MenuEntity;
import com.learn.service.MenuService;
import com.learn.vo.MenuVO;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDAO menuDAO;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuVO addMenu(MenuDTO menuDTO) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setIcon(menuDTO.getIcon());
        menuEntity.setName(menuDTO.getName());
        menuEntity.setParentId(menuDTO.getParentId());
        menuEntity.setPerms(menuDTO.getPerms());
        menuEntity.setPath(menuDTO.getPath());
        menuEntity.setOrderNum(menuDTO.getOrderNum());
        menuEntity.setType(menuDTO.getType());
        Long currentUserId = (Long) SecurityUtils.getSubject().getPrincipal();
        menuEntity.setCreateBy(currentUserId);
        menuEntity.setUpdateBy(currentUserId);
        menuDAO.addMenu(menuEntity);
        MenuVO menuVO = new MenuVO();
        menuVO.setMenuId(menuEntity.getMenuId());
        menuVO.setIcon(menuEntity.getIcon());
        menuVO.setName(menuEntity.getName());
        menuVO.setPath(menuEntity.getPath());
        menuVO.setType(menuEntity.getType());
        menuVO.setPerms(menuEntity.getPerms());
        menuVO.setOrderNum(menuEntity.getOrderNum());
        menuVO.setParentId(menuEntity.getParentId());
        return menuVO;
    }

    @Override
    public MenuVO getMenuByMenuId(Long menuId) {
        MenuEntity menuEntity = menuDAO.queryMenuByMenuId(menuId);
        if(menuEntity == null){
            throw new SqlSessionException("菜单不存在");
        }
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menuEntity,menuVO);
        return menuVO;
    }

    @Override
    public List<MenuVO> getMenus() {
        List<MenuEntity> menuEntities = menuDAO.queryMenus();
        List<MenuVO> menuVOS = new ArrayList<>();
        for(MenuEntity menu : menuEntities){
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu,menuVO);
            menuVOS.add(menuVO);
        }
        return menuVOS;
    }
    public Set<String> findPermsByUserId(Long userId){
        Set<String> perms = menuDAO.findPermsByUserId(userId);
        return perms == null ? Collections.emptySet() : perms;
    }

    @Override
    public List<MenuVO> findMenuTreeByUserId(Long userId) {
        List<MenuEntity> menus = menuDAO.findMenusByUserId(userId);
        Map<Long,MenuVO> map = new LinkedHashMap<>();
        for(MenuEntity m : menus){
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(m,menuVO);
            map.put(m.getMenuId(),menuVO);
        }
        List<MenuVO> tree = new ArrayList<>();
        for(MenuVO vo :map.values()){
            if(vo.getParentId() == null || vo.getParentId() == 0){
                tree.add(vo);
            }else{
                MenuVO parent = map.get(vo.getParentId());
                if(parent != null){
                    parent.getChildren().add(vo);
                }else{
                    tree.add(vo);
                }
            }
        }
        sortTree(tree);
        return tree;
    }

    private void sortTree(List<MenuVO> nodes){
        if(nodes == null || nodes.isEmpty())return ;
        nodes.sort(Comparator.comparingInt(m->m.getOrderNum() == null ? 0 : m.getOrderNum()));
        for(MenuVO node : nodes){
            sortTree(node.getChildren());
        }
    }
}
