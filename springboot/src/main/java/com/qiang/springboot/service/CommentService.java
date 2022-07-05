package com.qiang.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiang.springboot.entity.Comment;
import com.qiang.springboot.entity.Menu;
import com.qiang.springboot.mapper.CommentMapper;
import com.qiang.springboot.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-07-03 20:03
 */

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Resource
    private CommentMapper commentMapper;

    public List<Comment> findCommentDetail(Integer articleId) {
            return commentMapper.findCommentDetail(articleId);
    }
}
