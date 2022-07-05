package com.qiang.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiang.springboot.entity.Menu;
import com.qiang.springboot.entity.Role;
import com.qiang.springboot.mapper.MenuMapper;
import com.qiang.springboot.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XuQiang
 * @creat 2022-07-01 16:39
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    public List<Menu> findMenus(String name) {

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name", name);
        }
        List<Menu> list = list(queryWrapper);

        //找出pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for(Menu menu: parentNodes){
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
