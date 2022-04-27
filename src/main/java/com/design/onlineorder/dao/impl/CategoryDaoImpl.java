package com.design.onlineorder.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.design.onlineorder.dao.CategoryDao;
import com.design.onlineorder.entity.Category;
import com.design.onlineorder.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:12
 */
@Service
public class CategoryDaoImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryDao {
}
