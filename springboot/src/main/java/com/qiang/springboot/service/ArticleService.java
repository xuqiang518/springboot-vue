package com.qiang.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiang.springboot.entity.Article;
import com.qiang.springboot.entity.Menu;
import com.qiang.springboot.mapper.ArticleMapper;
import com.qiang.springboot.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
 * @author XuQiang
 * @creat 2022-07-03 11:01
 */

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {
}
