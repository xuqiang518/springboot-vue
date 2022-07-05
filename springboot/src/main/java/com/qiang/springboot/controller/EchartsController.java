package com.qiang.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.qiang.springboot.common.Constants;
import com.qiang.springboot.common.Result;
import com.qiang.springboot.config.AuthAccess;
import com.qiang.springboot.entity.User;
import com.qiang.springboot.mapper.FileMapper;
import com.qiang.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.provider.Sun;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-06-30 20:41
 */

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userService;
    @Resource
    private FileMapper fileMapper;

    @GetMapping("/example")
    public Result get(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members(){
        List<User> list = userService.list();
        int q1 = 0;  //第一季度
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for(User user: list){
            Date time = user.getTime();
            Quarter quarter = DateUtil.quarterEnum(time);
            switch (quarter){
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    @AuthAccess
    @GetMapping("/file/front/all")
    public Result frontAll() {
        return Result.success(fileMapper.selectList(null));
    }
}
