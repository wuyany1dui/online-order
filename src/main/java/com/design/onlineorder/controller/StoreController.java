package com.design.onlineorder.controller;

import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Created by DrEAmSs on 2022-04-26 13:14
 */
@Api(tags = "商家后台")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Resource
    private StoreService storeService;

    @ApiOperation("新建商店")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @ApiParam("店铺实体类") Store store) {
        storeService.create(store);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("获取当前用户的商店信息")
    @GetMapping("/query")
    public ResponseEntity<?> query() {
        return ResponseEntity.ok(storeService.query());
    }
}
