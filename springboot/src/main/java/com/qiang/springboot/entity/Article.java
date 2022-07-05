package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author XuQiang
 * @creat 2022-07-03 10:57
 */

@TableName("article")
@Data
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String content;
    private String user;
    private String time;
}
