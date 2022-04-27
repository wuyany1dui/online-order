package com.design.onlineorder.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.design.onlineorder.dao.CommentDao;
import com.design.onlineorder.entity.Comment;
import com.design.onlineorder.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:12
 */
@Service
public class CommentDaoImpl extends ServiceImpl<CommentMapper, Comment> implements CommentDao {
}
