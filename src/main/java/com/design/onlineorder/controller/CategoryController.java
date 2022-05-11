package com.design.onlineorder.controller;

import com.design.onlineorder.entity.Category;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-27 9:12
 */
@Api(tags = "分类")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @ApiOperation("创建分类")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @ApiParam("分类实体类") Category category) {
        categoryService.create(category);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("删除分类（同时会删除商品）")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody @ApiParam("分类id数组") List<String> ids) {
        categoryService.delete(ids);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("查询分类列表")
    @GetMapping("/queryList")
    public ResponseEntity<?> queryList(@RequestParam(required = false) @ApiParam("模糊查询分类名称") String name,
                                       @RequestParam(required = false) @ApiParam("当前页，从0开始") Integer pageIndex,
                                       @RequestParam(required = false) @ApiParam("当前页容量") Integer pageSize) {
        return ResponseEntity.ok(categoryService.queryList(name, pageIndex, pageSize));
    }

    @ApiOperation("查询分类详情")
    @GetMapping("/query/{id}")
    public ResponseEntity<?> query(@PathVariable @ApiParam("分类id") String id) {
        return ResponseEntity.ok(categoryService.query(id));
    }
}
