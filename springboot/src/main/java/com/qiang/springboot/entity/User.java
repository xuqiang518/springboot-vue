package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author XuQiang
 * @creat 2022-06-26 8:46
 */

@Data
@TableName(value = "sys_user")
@ToString
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;

    //@JsonIgnore  //忽略密码不显示出来
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private Date time;

    @TableField(value = "avatar_url")  //指定数据库的字段名称
    private String avatar;

    private String role;

}
