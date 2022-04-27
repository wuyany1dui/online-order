package com.design.onlineorder.controller;

import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.CommentService;
import com.design.onlineorder.vo.CommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:39
 */
@Api(tags = "评论")
@RestController
@RequestMapping("/category")
public class CommentController {

    @Resource
    private CommentService commentService;

    @ApiOperation("创建评论")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @ApiParam("评论vo") CommentVo commentVo) {
        commentService.create(commentVo);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("查询评论列表")
    @GetMapping("/queryList")
    public ResponseEntity<?> queryList(@RequestParam(required = false) @ApiParam("商品id") String orderId,
                                       @RequestParam(required = false) @ApiParam("餐品id") String productId,
                                       @RequestParam @ApiParam("当前页数，从0开始") Integer pageIndex,
                                       @RequestParam @ApiParam("当前页容量") Integer pageSize) {
        return ResponseEntity.ok(commentService.queryList(orderId, productId, pageIndex, pageSize));
    }
}
