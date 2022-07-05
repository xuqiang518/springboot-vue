package com.qiang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-07-03 19:59
 */

@Data
@TableName("t_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String content;
    private Integer userId;
    private String time;
    private Integer pid;

    @TableField(exist = false)
    private String pNickname;

    @TableField(exist = false)
    private Integer pUserId;

    private Integer originId;
    private Integer articleId;

    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String avatarUrl;
    @TableField(exist = false)
    private List<Comment> children;

}
