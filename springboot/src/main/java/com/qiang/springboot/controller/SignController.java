package com.qiang.springboot.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiang.springboot.common.Constants;
import com.qiang.springboot.common.Result;
import com.qiang.springboot.entity.Sign;
import com.qiang.springboot.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XuQiang
 * @creat 2022-07-01 9:52
 */

@RequestMapping("/sign")
@RestController
public class SignController {

    @Autowired
    private SignService signService;

    @PostMapping
    public Result save(@RequestBody Sign sign){
        if(sign.getId() == null){
            String today = DateUtil.today();
            QueryWrapper<Sign> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", sign.getUsername());
            queryWrapper.eq("time", sign.getTime());
            Sign one = signService.getOne(queryWrapper);
            if(one != null){
                return Result.error(Constants.CODE_600, "您已经打过卡了");
            }
            sign.setTime(today);
        }
        signService.saveOrUpdate(sign);
        return Result.success();
    }
}
