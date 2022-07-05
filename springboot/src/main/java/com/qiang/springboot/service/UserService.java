package com.qiang.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiang.springboot.common.Constants;
import com.qiang.springboot.common.RoleEnum;
import com.qiang.springboot.controller.dto.UserDTO;
import com.qiang.springboot.controller.dto.UserPasswordDTO;
import com.qiang.springboot.entity.Menu;
import com.qiang.springboot.entity.User;
import com.qiang.springboot.exception.ServiceException;
import com.qiang.springboot.mapper.RoleMapper;
import com.qiang.springboot.mapper.RoleMenuMapper;
import com.qiang.springboot.mapper.UserMapper;
import com.qiang.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-06-26 10:00
 */

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private UserMapper userMapper;

    public boolean saveUser(User user) {
//        if(user.getId() != null){
//            return save(user);     // mybatis-plus提供的方法，表示插入数据
//        }else {
//            return updateById(user);
//        }
        return saveOrUpdate(user);

    }

    public UserDTO login(UserDTO userDTO) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", userDTO.getUsername());
//        queryWrapper.eq("password",userDTO.getPassword());
//        List<User> list = list(queryWrapper);
//        return list.size() != 0;
        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        User one = getUserInfo(userDTO);
//        try{
//            one = getOne(queryWrapper);  //从数据库查询的用户信息
//
//        }catch (Exception e){
//            LOG.error(e);
//            throw new ServiceException(Constants.CODE_500, "系统错误");
//        }
        if(one != null){
            BeanUtil.copyProperties(one, userDTO,true);
            //设置 token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole();

            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);

            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    public User register(UserDTO userDTO) {
        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));  //用户密码加密
        User one = getUserInfo(userDTO);
        if(one == null){
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            one.setRole(RoleEnum.ROLE_USER.toString());  //默认一个角色为普通用户
            if(one.getNickname() == null){
                one.setNickname(one.getUsername());
            }
            save(one);
        }else {
            throw new ServiceException(Constants.CODE_600, "用户已经存在");
        }
        return one;
    }
    
    /*@Autowired
    private UserMapper userMapper;

    public int save(User user){   //如果user没有id，则表示新增
        if(user.getId() == null){
            return userMapper.insert(user);
        }else{  //更新
            return userMapper.update(user);
        }
    }*/

    private User getUserInfo(UserDTO userDTO){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try{
            one = getOne(queryWrapper);  //数据库中查询用户信息
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**获取当前角色的菜单列表**/
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");

        List<Menu> roleMenus = new ArrayList<>();  //
        //筛选当前用户角色的菜单
        for(Menu menu: menus){
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if(update < 1){
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }
}
