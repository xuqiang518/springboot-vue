package com.qiang.springboot.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiang.springboot.entity.Menu;
import com.qiang.springboot.entity.Role;
import com.qiang.springboot.entity.RoleMenu;
import com.qiang.springboot.mapper.RoleMapper;
import com.qiang.springboot.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-06-30 22:07
 */

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    @Transactional
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id", roleId);
//        roleMenuMapper.delete(queryWrapper);

        //先删除当前所有id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);

        //再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for(Integer menuId: menuIds){
            Menu menu = menuService.getById(menuId);
            if(menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())){ //二级菜单  并且传过来的menuId数组里面没有它的父级id
                //那么我们就要补上这个父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }

            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    //根据roleId查询其绑定的menuId
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
