package com.design.onlineorder.controller;

import com.design.onlineorder.entity.Product;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.ProductService;
import com.design.onlineorder.vo.ProductListQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:03
 */
@Api(tags = "餐品管理")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @ApiOperation("新增餐品")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @ApiParam("餐品实体类") Product product) {
        productService.create(product);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("查询餐品列表")
    @PostMapping("/queryList")
    public ResponseEntity<?> queryList(@RequestBody @ApiParam("餐品列表查询实体类") ProductListQueryVo productListQueryVo) {
        return ResponseEntity.ok(productService.queryList(productListQueryVo));
    }
}
