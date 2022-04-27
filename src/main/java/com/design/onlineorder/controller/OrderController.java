package com.design.onlineorder.controller;

import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.OrderService;
import com.design.onlineorder.vo.OrderListQueryVo;
import com.design.onlineorder.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:19
 */
@Api(tags = "订单")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @ApiParam("订单Vo类") OrderVo orderVo) {
        return ResponseEntity.ok(orderService.create(orderVo));
    }

    @ApiOperation("支付订单")
    @PostMapping("/pay/{id}")
    public ResponseEntity<?> pay(@PathVariable @ApiParam("订单id") String id) {
        orderService.pay(id);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("查询订单列表")
    @PostMapping("/queryList")
    public ResponseEntity<?> queryList(@RequestBody @ApiParam("订单列表查询Vo类") OrderListQueryVo orderListQueryVo) {
        return ResponseEntity.ok(orderService.queryList(orderListQueryVo));
    }
}
