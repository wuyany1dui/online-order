package com.design.onlineorder.controller;

import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.StoreService;
import com.design.onlineorder.vo.StoreListQueryVo;
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

    @ApiOperation("获取商店列表")
    @PostMapping("/queryList")
    public ResponseEntity<?> queryList (@RequestBody @ApiParam("商店查询类") StoreListQueryVo storeListQueryVo) {
        return ResponseEntity.ok(storeService.queryList(storeListQueryVo));
    }

    @ApiOperation("获取首页人气商店列表")
    @GetMapping("/queryFirstPageList")
    public ResponseEntity<?> queryFirstPageList() {
        return ResponseEntity.ok(storeService.queryFirstPageList());
    }
}
