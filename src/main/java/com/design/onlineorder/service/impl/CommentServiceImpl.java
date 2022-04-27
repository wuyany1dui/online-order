package com.design.onlineorder.service.impl;

import com.design.onlineorder.dao.CommentDao;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.entity.Comment;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.enums.OrderStatusEnum;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.CommentService;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.CommentVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:40
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private ProductDao productDao;

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
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setUserId(UserUtils.getCurrentUser().getId());
        comment.setNickname(UserUtils.getCurrentUser().getNickname());
        comment.setUsername(UserUtils.getCurrentUser().getUsername());
        comment.setOrderId(commentVo.getOrderId());
        Optional<Product> productOpt = productDao.lambdaQuery().eq(Product::getId, commentVo.getProductId()).oneOpt();
        if (productOpt.isEmpty()) {
            throw new MyException(400, ResultEnum.PRODUCT_NOT_EXISTS.getLabel());
        } else {
            comment.setProductId(productOpt.get().getId());
            comment.setProductName(productOpt.get().getName());
        }
        commentDao.save(comment);
    }
}
