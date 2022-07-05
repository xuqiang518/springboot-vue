package com.qiang.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiang.springboot.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-07-03 20:02
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select c.*, u.nickname, u.avatar_url from t_comment c left join sys_user u on c.user_id = u.id where c.article_id = #{articleId} order by id desc")
    List<Comment> findCommentDetail(@Param("articleId") Integer articleId);
}
