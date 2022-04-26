package com.design.onlineorder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.enums.UserTypeEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.ProductService;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.ProductListPageVo;
import com.design.onlineorder.vo.ProductListQueryVo;
import com.design.onlineorder.vo.ProductListVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:04
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;

    @Resource
    private StoreDao storeDao;

    @Override
    public void create(Product product) {
        Optional<Store> store = storeDao.lambdaQuery()
                .eq(Store::getUserId, UserUtils.getCurrentUser().getId())
                .oneOpt();
        if (store.isEmpty()) {
            throw new MyException(400, ResultEnum.STORE_NOT_EXISTS.getLabel());
        } else {
            product.setStoreId(store.get().getId());
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        product.setCreateTime(timestamp);
        product.setUpdateTime(timestamp);
        productDao.save(product);
    }

    @Override
    public ProductListPageVo queryList(ProductListQueryVo productListQueryVo) {
        LambdaQueryChainWrapper<Product> productLambdaQueryChainWrapper = productDao.lambdaQuery()
                .eq(StringUtils.isNotBlank(productListQueryVo.getId()), Product::getId, productListQueryVo.getId())
                .eq(StringUtils.isNotBlank(productListQueryVo.getStoreId()), Product::getStoreId, productListQueryVo.getStoreId())
                .like(StringUtils.isNotBlank(productListQueryVo.getType()), Product::getType, productListQueryVo.getType());
        if (Objects.nonNull(productListQueryVo.getStartPrice()) && Objects.nonNull(productListQueryVo.getEndPrice())) {
            productLambdaQueryChainWrapper.gt(Product::getPrice, productListQueryVo.getStartPrice())
                    .le(Product::getPrice, productListQueryVo.getEndPrice());
        } else if (Objects.nonNull(productListQueryVo.getStartPrice()) && Objects.isNull(productListQueryVo.getEndPrice())) {
            productLambdaQueryChainWrapper.gt(Product::getPrice, productListQueryVo.getStartPrice());
        } else if (Objects.isNull(productListQueryVo.getStartPrice()) && Objects.nonNull(productListQueryVo.getEndPrice())) {
            productLambdaQueryChainWrapper.le(Product::getPrice, productListQueryVo.getEndPrice());
        }
        List<ProductListVo> productListVos = Lists.newArrayList();
        productLambdaQueryChainWrapper.list().forEach(temp -> {
            ProductListVo productListVo = new ProductListVo();
            BeanUtils.copyProperties(temp, productListVo);
            productListVos.add(productListVo);
        });
        return new ProductListPageVo(productListVos.size(),
                productListVos.stream()
                        .skip((long) productListQueryVo.getPageIndex() * productListQueryVo.getPageSize())
                        .limit(productListQueryVo.getPageSize())
                        .collect(Collectors.toList()));
    }
}
