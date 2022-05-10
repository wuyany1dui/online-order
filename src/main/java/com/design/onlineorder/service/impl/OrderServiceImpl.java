package com.design.onlineorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.entity.Product;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:21
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private ProductDao productDao;

    @Override
    public String create(OrderVo orderVo) {
        Order order = new Order();
        BeanUtils.copyProperties(orderVo, order);
        String id = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.isNotBlank(orderVo.getId())) {
            id = orderVo.getId();
        } else {
            order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        order.setId(id);
        order.setProductInfo(JSON.toJSONString(orderVo.getProductInfos()));
        order.setStatus(OrderStatusEnum.NOT_PAID);
        List<Product> products = productDao.lambdaQuery()
                .in(Product::getId, orderVo.getProductInfos().stream().map(OrderProductInfoVo::getProductId)
                        .collect(Collectors.toList()))
                .list();
        AtomicReference<BigDecimal> price = new AtomicReference<>(BigDecimal.valueOf(0));
        AtomicReference<Float> tempPrice = new AtomicReference<>(0F);
        orderVo.getProductInfos().forEach(temp -> {
            Optional<Product> optional = products.stream()
                    .filter(tempProduct -> temp.getProductId().equals(tempProduct.getId()))
                    .findFirst();
            optional.ifPresent(product ->
                    tempPrice.updateAndGet(v -> v + temp.getCount() * product.getPrice().floatValue()));
        });
        price.set(BigDecimal.valueOf(tempPrice.get()));
        order.setPrice(price.get());
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
                .orderBy(true, false, Order::getCreateTime)
                .list();
        orders.stream()
                .filter(tempOrder -> Objects.equals(tempOrder.getStatus(), OrderStatusEnum.NOT_PAID))
                .collect(Collectors.toList())
                .forEach(tempOrder -> {
                    OrderVo orderVo = new OrderVo();
                    BeanUtils.copyProperties(tempOrder, orderVo);
                    orderVo.setProductInfos(JSON.parseObject(tempOrder.getProductInfo(),
                            new TypeReference<List<OrderProductInfoVo>>() {
                            }));
                    data.add(orderVo);
                });
        orders.stream()
                .filter(tempOrder -> !Objects.equals(tempOrder.getStatus(), OrderStatusEnum.NOT_PAID))
                .collect(Collectors.toList())
                .forEach(tempOrder -> {
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

    @Override
    public List<OrderProductInfoVo> queryDetail(String id) {
        Order order = orderDao.lambdaQuery().eq(Order::getId, id).one();
        List<OrderProductInfoVo> orderProductInfoVos = JSON.parseObject(order.getProductInfo(),
                new TypeReference<List<OrderProductInfoVo>>() {
                });
        List<Product> products = productDao.lambdaQuery()
                .in(Product::getId, orderProductInfoVos.stream().map(OrderProductInfoVo::getProductId)
                        .collect(Collectors.toList()))
                .list();
        orderProductInfoVos.forEach(temp -> {
            Optional<Product> optional = products.stream()
                    .filter(tempProduct -> temp.getProductId().equals(tempProduct.getId()))
                    .findFirst();
            optional.ifPresent(o -> {
                if (StringUtils.isNotBlank(o.getFirstImage())) {
                    temp.setFirstImage(o.getFirstImage());
                }
                temp.setPrice(o.getPrice());
            });
        });
        return orderProductInfoVos;
    }
}
