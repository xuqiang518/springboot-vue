package com.qiang.springboot.utils;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.qiang.springboot.entity.User;
import com.qiang.springboot.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.PortUnreachableException;
import java.util.Date;

/**
 * @author XuQiang
 * @creat 2022-06-29 19:09
 */

/**生成 token */
@Component
public class TokenUtils {

    private static UserService staticUserService;

    @Resource
    private UserService userService;

    @PostConstruct
    public void setUserService(){
        staticUserService = userService;
    }

    public static String genToken(String userId, String sign){
        return JWT.create().withAudience(userId)                                //将user id 保存到 token 里面，作为载荷
                    .withExpiresAt(DateUtil.offsetHour(new Date(), 2))   //两小时 token 过期
                    .sign(Algorithm.HMAC256(sign));                            //以 password 作为 token 的 密钥
    }

    /**获取当前登录用户信息*/
    public static User getCurrentUser(){
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if(StrUtil.isNotBlank(token)){
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(userId);

            }
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
