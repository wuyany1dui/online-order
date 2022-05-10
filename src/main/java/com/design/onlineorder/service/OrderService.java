package com.design.onlineorder.service;

import com.design.onlineorder.vo.OrderListPageVo;
import com.design.onlineorder.vo.OrderListQueryVo;
import com.design.onlineorder.vo.OrderProductInfoVo;
import com.design.onlineorder.vo.OrderVo;

import java.util.List;

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

    /**
     * 查询订单列表
     */
    OrderListPageVo queryList(OrderListQueryVo orderListQueryVo);

    /**
     * 查询订单详情
     */
    List<OrderProductInfoVo> queryDetail(String id);
}
