package com.design.onlineorder.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:12
 */
@Service
public class OrderDaoImpl extends ServiceImpl<OrderMapper, Order> implements OrderDao {
}
