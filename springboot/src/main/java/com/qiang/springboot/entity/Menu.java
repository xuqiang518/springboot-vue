package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-07-01 16:36
 */

@Data
@TableName("sys_menu")
public class Menu {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String path;
    private String icon;
    private String description;
    private Integer pid;

    @TableField(exist = false)
    private List<Menu> children;

    private String pagePath;


}
