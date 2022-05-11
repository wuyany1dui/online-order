package com.design.onlineorder.service.impl;

import com.design.onlineorder.dao.CategoryDao;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.entity.Category;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.enums.UserTypeEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.CategoryService;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.CategoryListPageVo;
import com.design.onlineorder.vo.CategoryListVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by DrEAmSs on 2022-04-27 9:13
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private StoreDao storeDao;

    @Override
    public void create(Category category) {
        if (!Objects.equals(UserUtils.getCurrentUser().getType(), UserTypeEnum.ADMIN)) {
            throw new MyException(400, ResultEnum.ACCESS_DENIED.getLabel());
        }
        if (StringUtils.isBlank(category.getName())) {
            throw new MyException(400, "分类名不能为空！");
        }
        Optional<Category> categoryOpt = categoryDao.lambdaQuery().eq(Category::getName, category.getName()).oneOpt();
        if (categoryOpt.isPresent()) {
            throw new MyException(400, ResultEnum.CATEGORY_EXISTS.getLabel());
        }
        if (StringUtils.isBlank(category.getId())) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            category.setCreateTime(timestamp);
            category.setUpdateTime(timestamp);
            category.setId(UUID.randomUUID().toString());
        } else {
            category.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        category.setDeleted(0);
        categoryDao.saveOrUpdate(category);
    }

    @Override
    public void delete(List<String> ids) {
        List<Category> categories = categoryDao.lambdaQuery().in(Category::getId, ids).list();
        List<String> categoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());
        List<Product> products = productDao.lambdaQuery().list();
        List<Store> stores = storeDao.lambdaQuery().list();
        categoryNames.forEach(tempCategoryName -> {
            products.stream()
                    .filter(tempProduct -> tempProduct.getType().contains(tempCategoryName))
                    .collect(Collectors.toList())
                    .forEach(tempProduct -> {
                        List<String> types = new ArrayList<>(Arrays.asList(tempProduct.getType().split(",")));
                        types.removeIf(temp -> temp.equals(tempCategoryName));
                        tempProduct.setType(String.join(",", types));
                    });
            stores.stream()
                    .filter(tempStore -> tempStore.getType().contains(tempCategoryName))
                    .collect(Collectors.toList())
                    .forEach(tempStore -> {
                        List<String> types = new ArrayList<>(Arrays.asList(tempStore.getType().split(",")));
                        types.removeIf(temp -> temp.equals(tempCategoryName));
                        tempStore.setType(String.join(",", types));
                    });
        });
        categoryDao.removeBatchByIds(ids);
        productDao.saveOrUpdateBatch(products);
        storeDao.saveOrUpdateBatch(stores);
    }

    @Override
    public CategoryListPageVo queryList(String name, Integer pageIndex, Integer pageSize) {
        List<CategoryListVo> categoryListVos = Lists.newArrayList();
        List<Category> categories = categoryDao.lambdaQuery()
                .like(StringUtils.isNotBlank(name), Category::getName, name)
                .orderBy(true, false, Category::getUpdateTime)
                .list();
        categories.forEach(temp -> {
            CategoryListVo categoryListVo = new CategoryListVo();
            BeanUtils.copyProperties(temp, categoryListVo);
            categoryListVos.add(categoryListVo);
        });
        if (Objects.nonNull(pageIndex) && Objects.nonNull(pageSize)) {
            return new CategoryListPageVo(categoryListVos.size(),
                    categoryListVos.stream().skip((long) (pageIndex - 1) * pageSize).limit(pageSize)
                            .collect(Collectors.toList()));
        } else {
            return new CategoryListPageVo(categoryListVos.size(), categoryListVos);
        }
    }

    @Override
    public Category query(String id) {
        Optional<Category> categoryOpt = categoryDao.lambdaQuery().eq(Category::getId, id).oneOpt();
        if (categoryOpt.isEmpty()) {
            throw new MyException(400, ResultEnum.CATEGORY_NOT_EXISTS.getLabel());
        } else {
            return categoryOpt.get();
        }
    }
}
