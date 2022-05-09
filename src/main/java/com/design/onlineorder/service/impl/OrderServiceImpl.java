package com.design.onlineorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.enums.OrderStatusEnum;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.OrderService;
import com.design.onlineorder.vo.OrderListPageVo;
import com.design.onlineorder.vo.OrderListQueryVo;
import com.design.onlineorder.vo.OrderProductInfoVo;
import com.design.onlineorder.vo.OrderVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        orderDao.saveOrUpdate(order);
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

    @Override
    public OrderListPageVo queryList(OrderListQueryVo orderListQueryVo) {
        List<OrderVo> data = Lists.newArrayList();
        List<Order> orders = orderDao.lambdaQuery()
                .eq(StringUtils.isNotBlank(orderListQueryVo.getId()), Order::getId, orderListQueryVo.getId())
                .eq(StringUtils.isNotBlank(orderListQueryVo.getUserId()), Order::getUserId, orderListQueryVo.getUserId())
                .eq(Objects.nonNull(orderListQueryVo.getStatus()), Order::getStatus, orderListQueryVo.getStatus())
                .list();
        orders.forEach(tempOrder -> {
            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(tempOrder, orderVo);
            orderVo.setProductInfos(JSON.parseObject(tempOrder.getProductInfo(),
                    new TypeReference<List<OrderProductInfoVo>>() {
                    }));
            data.add(orderVo);
        });
        return new OrderListPageVo(data.size(), data.stream()
                .skip((long) (orderListQueryVo.getPageIndex() - 1) * orderListQueryVo.getPageSize())
                .limit(orderListQueryVo.getPageSize())
                .collect(Collectors.toList()));
    }
}
