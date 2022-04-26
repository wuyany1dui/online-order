package com.design.onlineorder.service;

import com.design.onlineorder.vo.OrderVo;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:20
 */
public interface OrderService {

    /**
     * 创建订单返回订单id
     */
    String create(OrderVo orderVo);

    /**
     * 支付订单
     */
    void pay(String id);
}
