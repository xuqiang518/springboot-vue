package com.qiang.springboot.controller.dto;

import lombok.Data;

/**
 * @author XuQiang
 * @creat 2022-07-04 8:10
 */
@Data
public class UserPasswordDTO {
    private String username;
    private String password;
    private String newPassword;
}
