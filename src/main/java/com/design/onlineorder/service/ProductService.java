package com.design.onlineorder.service;

import com.design.onlineorder.entity.Product;
import com.design.onlineorder.vo.ProductListPageVo;
import com.design.onlineorder.vo.ProductListQueryVo;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:04
 */
public interface ProductService {

    /**
     * 新建餐品
     */
    void create(Product product);

    ProductListPageVo queryList(ProductListQueryVo productListQueryVo);
}
