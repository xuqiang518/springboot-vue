package com.qiang.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.qiang.springboot.common.Constants;
import com.qiang.springboot.config.AuthAccess;
import com.qiang.springboot.entity.User;
import com.qiang.springboot.exception.ServiceException;
import com.qiang.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XuQiang
 * @creat 2022-06-29 19:33
 */

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        //如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }else {

            HandlerMethod h = (HandlerMethod) handler;
            AuthAccess authAccess = h.getMethodAnnotation(AuthAccess.class);
            if(authAccess != null){
                return true;
            }
        }
        // 执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401, "没有token， 请查询登录！ ");
        }

        //获取 token 中的 id
        String userId;
        try{
           userId = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException e){
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录！");
        }
        //根据token 中的userId 查询数据库
        User user = userService.getById(userId);
        if(user == null){
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录！");
        }

        //用户密码加签验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);  // 验证token
        }catch (JWTVerificationException e){
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录！");
        }
        return true;
    }
}
