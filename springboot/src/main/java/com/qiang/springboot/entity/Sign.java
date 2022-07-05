package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author XuQiang
 * @creat 2022-07-01 9:46
 */
@Data
@TableName("sign")
public class Sign {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String time;
    private String comment;
}
