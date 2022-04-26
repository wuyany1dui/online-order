package com.design.onlineorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.enums.OrderStatusEnum;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.OrderService;
import com.design.onlineorder.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:21
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Override
    public String create(OrderVo orderVo) {
        Order order = new Order();
        BeanUtils.copyProperties(orderVo, order);
        String id = UUID.randomUUID().toString().replace("-", "");
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        order.setId(id);
        order.setProductInfo(JSON.toJSONString(orderVo.getProductInfos()));
        order.setStatus(OrderStatusEnum.NOT_PAID);
        orderDao.save(order);
        return id;
    }

    @Override
    public void pay(String id) {
        Optional<Order> orderOpt = orderDao.lambdaQuery().eq(Order::getId, id).oneOpt();
        if (orderOpt.isEmpty()) {
            throw new MyException(400, ResultEnum.ORDER_NOT_EXISTS.getLabel());
        } else {
            if (Objects.equals(orderOpt.get().getStatus(), OrderStatusEnum.PAID)) {
                throw new MyException(400, ResultEnum.ORDER_ALREADY_PAID.getLabel());
            } else {
                orderDao.lambdaUpdate().eq(Order::getId, id)
                        .set(Order::getPayTime, new Timestamp(System.currentTimeMillis()))
                        .set(Order::getStatus, OrderStatusEnum.PAID)
                        .update();
            }
        }
    }
}
