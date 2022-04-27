package com.design.onlineorder.service;

import com.design.onlineorder.vo.CommentVo;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:39
 */
public interface CommentService {

    /**
     * 创建评论
     */
    void create(CommentVo commentVo);
}
