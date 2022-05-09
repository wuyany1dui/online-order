package com.design.onlineorder.service.impl;

import com.design.onlineorder.dao.CommentDao;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.entity.Comment;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.enums.OrderStatusEnum;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.enums.UserTypeEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.CommentService;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.CommentListPageVo;
import com.design.onlineorder.vo.CommentVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:40
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private OrderDao orderDao;

    @Override
    public void create(CommentVo commentVo) {
        Optional<Order> orderOpt = orderDao.lambdaQuery().eq(Order::getId, commentVo.getOrderId()).oneOpt();
        if (orderOpt.isEmpty()) {
            throw new MyException(400, ResultEnum.ORDER_NOT_EXISTS.getLabel());
        } else {
            if (!Objects.equals(orderOpt.get().getStatus(), OrderStatusEnum.PAID)) {
                throw new MyException(400, ResultEnum.ORDER_NOT_PAID_OR_OVERTIME.getLabel());
            }
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVo, comment);
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setUserId(UserUtils.getCurrentUser().getId());
        comment.setNickname(UserUtils.getCurrentUser().getNickname());
        comment.setUsername(UserUtils.getCurrentUser().getUsername());
        comment.setOrderId(commentVo.getOrderId());
        commentDao.save(comment);
    }

    @Override
    public CommentListPageVo queryList(String orderId, String productId, String userId, Integer pageIndex, Integer pageSize) {
        List<String> orderIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(productId)) {
            orderIds.addAll(orderDao.lambdaQuery().like(Order::getProductInfo, productId)
                    .list().stream().map(Order::getId)
                    .collect(Collectors.toList()));
        }
        List<Comment> comments = commentDao.lambdaQuery()
                .eq(StringUtils.isNotBlank(userId), Comment::getUserId, userId)
                .eq(StringUtils.isNotBlank(orderId), Comment::getOrderId, orderId)
                .in(CollectionUtils.isNotEmpty(orderIds), Comment::getOrderId, orderIds).list();
        return new CommentListPageVo(comments.size(), comments.stream().skip((long) pageIndex * pageSize)
                .limit(pageSize).collect(Collectors.toList()));
    }

    @Override
    public void delete(List<String> ids) {
        if (!Objects.equals(UserUtils.getCurrentUser().getType(), UserTypeEnum.ADMIN)) {
            List<Comment> comments = commentDao.lambdaQuery().in(Comment::getId, ids).list();
            if (comments.stream().map(Comment::getUserId)
                    .anyMatch(temp -> !temp.equals(UserUtils.getCurrentUser().getId()))) {
                throw new MyException(400, ResultEnum.ACCESS_DENIED.getLabel());
            }
        }
        commentDao.removeBatchByIds(ids);
    }

    @Override
    public Boolean checkComment(String productId) {
        List<String> productInfos = orderDao.lambdaQuery()
                .in(Order::getUserId, UserUtils.getCurrentUser().getId())
                .eq(Order::getStatus, OrderStatusEnum.PAID)
                .list().stream()
                .map(Order::getProductInfo)
                .collect(Collectors.toList());
        return productInfos.stream().anyMatch(temp -> temp.contains(productId));
    }
}
