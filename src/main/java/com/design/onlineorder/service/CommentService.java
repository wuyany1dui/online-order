package com.design.onlineorder.service;

import com.design.onlineorder.vo.CommentListPageVo;
import com.design.onlineorder.vo.CommentVo;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:39
 */
public interface CommentService {

    /**
     * 创建评论
     */
    void create(CommentVo commentVo);

    /**
     * 查询评论列表
     */
    CommentListPageVo queryList(String orderId, String productId,String userId, Integer pageIndex, Integer pageSize);

    /**
     * 删除评论
     */
    void delete(List<String> ids);
}
