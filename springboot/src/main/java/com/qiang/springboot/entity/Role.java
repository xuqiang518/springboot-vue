package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.naming.ldap.PagedResultsControl;

/**
 * @author XuQiang
 * @creat 2022-06-30 21:54
 */

@Data
@TableName("sys_role")
public class Role {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;

    private String flag;  //唯一标识符
}
