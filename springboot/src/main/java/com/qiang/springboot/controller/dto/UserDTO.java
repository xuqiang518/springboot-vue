package com.qiang.springboot.controller.dto;

import com.qiang.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-06-28 9:29
 */
/** 接收前端登录请求的参数*/
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String token;

    private List<Menu> menus;
    private String role;


}
