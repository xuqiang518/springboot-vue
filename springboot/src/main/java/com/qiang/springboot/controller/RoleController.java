package com.qiang.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiang.springboot.common.Result;
import com.qiang.springboot.entity.Role;
import com.qiang.springboot.mapper.RoleMapper;
import com.qiang.springboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-06-30 22:03
 */

@RequestMapping("/role")
@RestController
public class RoleController {

    @Resource
    private RoleMapper roleMapper;
    @Autowired
    private RoleService roleService;

    @PostMapping        //更新或新增
    public Result save(@RequestBody Role role){
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")         //删除
    public Result delete(@PathVariable Integer id){
        roleService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")   //批量删除
    public Result deleteBatch(@RequestBody List<Integer> ids){
        roleService.removeByIds(ids);
        return Result.success();
    }



    @GetMapping   //查询所有
    public Result findAll(){
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")   //查询单个
    public Result findOne(@PathVariable Integer id){
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")   //分页查询
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize){
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**绑定角色和菜单的关系**/
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds){
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleService.getRoleMenu(roleId));
    }
}
