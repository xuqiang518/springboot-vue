package com.qiang.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiang.springboot.entity.Sign;
import com.qiang.springboot.mapper.SignMapper;
import org.springframework.stereotype.Service;

/**
 * @author XuQiang
 * @creat 2022-07-01 9:55
 */

@Service
public class SignService extends ServiceImpl<SignMapper, Sign> {
}
