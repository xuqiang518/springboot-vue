package com.qiang.springboot.controller;

import ch.qos.logback.classic.sift.AppenderFactoryUsingJoran;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiang.springboot.common.Constants;
import com.qiang.springboot.common.Result;
import com.qiang.springboot.entity.Dict;
import com.qiang.springboot.entity.Menu;
import com.qiang.springboot.mapper.DictMapper;
import com.qiang.springboot.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XuQiang
 * @creat 2022-07-01 16:39
 */

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Resource
    private DictMapper dictMapper;

    @PostMapping   //新增或更新
    public Result save(@RequestBody Menu menu){
        menuService.saveOrUpdate(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")   //删除
    public Result delete(@PathVariable Integer id){
        menuService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch") // 批量删除
    public Result deleteBatch(@RequestBody List<Integer> ids){
        menuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/ids")
    public Result findAllIds(){
        return Result.success(menuService.list().stream().map(Menu::getId));
    }

    @GetMapping    //查询所有
    public Result findAll(@RequestParam(defaultValue = "") String name){

        List<Menu> parentNodes = menuService.findMenus(name);
        return Result.success(parentNodes);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(null));
    }
}
