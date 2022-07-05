package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author XuQiang
 * @creat 2022-07-01 21:30
 */

@TableName("sys_dict")
@Data
public class Dict {
    private String name;
    private String value;
    private String type;
}
