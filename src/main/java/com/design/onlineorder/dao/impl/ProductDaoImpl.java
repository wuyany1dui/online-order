package com.design.onlineorder.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:12
 */
@Service
public class ProductDaoImpl extends ServiceImpl<ProductMapper, Product> implements ProductDao {
}
