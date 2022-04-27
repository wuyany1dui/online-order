package com.design.onlineorder.service;

import com.design.onlineorder.entity.Category;
import com.design.onlineorder.vo.CategoryListPageVo;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-27 9:13
 */
public interface CategoryService {

    /**
     * 创建分类
     */
    void create(Category category);

    /**
     * 删除分类
     */
    void delete(List<String> ids);

    /**
     * 分页查询分类列表
     */
    CategoryListPageVo queryList(String name, Integer pageIndex, Integer pageSize);

    /**
     * 查询分类详情
     */
    Category query(String id);
}
