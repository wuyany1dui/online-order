package com.design.onlineorder.service;

import com.design.onlineorder.entity.Product;
import com.design.onlineorder.vo.ProductListPageVo;
import com.design.onlineorder.vo.ProductListQueryVo;
import com.design.onlineorder.vo.ProductListVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:04
 */
public interface ProductService {

    /**
     * 新建餐品
     */
    void create(Product product);

    ProductListPageVo queryList(ProductListQueryVo productListQueryVo);

    void uploadAvatar(MultipartFile multipartFile, String id);

    List<Product> queryFirstPageList();
}
