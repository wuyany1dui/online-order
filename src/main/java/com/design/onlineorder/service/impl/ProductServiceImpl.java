package com.design.onlineorder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.dao.UserDao;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.entity.User;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.ProductService;
import com.design.onlineorder.utils.FileUtils;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.ProductListPageVo;
import com.design.onlineorder.vo.ProductListQueryVo;
import com.design.onlineorder.vo.ProductListVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Resource
    private UserDao userDao;

    @Value("${onlineOrder.filePath}")
    private String filePath;

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
        if (StringUtils.isNotBlank(product.getId())) {
            product.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            product.setCreateTime(timestamp);
            product.setUpdateTime(timestamp);
        }
        productDao.saveOrUpdate(product);
    }

    @Override
    public ProductListPageVo queryList(ProductListQueryVo productListQueryVo) {
        LambdaQueryChainWrapper<Product> productLambdaQueryChainWrapper = productDao.lambdaQuery()
                .eq(StringUtils.isNotBlank(productListQueryVo.getName()), Product::getName, productListQueryVo.getName())
                .eq(StringUtils.isNotBlank(productListQueryVo.getId()), Product::getId, productListQueryVo.getId())
                .eq(StringUtils.isNotBlank(productListQueryVo.getStoreId()), Product::getStoreId, productListQueryVo.getStoreId())
                .like(StringUtils.isNotBlank(productListQueryVo.getType()), Product::getType, productListQueryVo.getType());
        if (Objects.nonNull(productListQueryVo.getStartPrice()) && Objects.nonNull(productListQueryVo.getEndPrice())) {
            productLambdaQueryChainWrapper.ge(Product::getPrice, productListQueryVo.getStartPrice())
                    .le(Product::getPrice, productListQueryVo.getEndPrice());
        } else if (Objects.nonNull(productListQueryVo.getStartPrice()) && Objects.isNull(productListQueryVo.getEndPrice())) {
            productLambdaQueryChainWrapper.ge(Product::getPrice, productListQueryVo.getStartPrice());
        } else if (Objects.isNull(productListQueryVo.getStartPrice()) && Objects.nonNull(productListQueryVo.getEndPrice())) {
            productLambdaQueryChainWrapper.le(Product::getPrice, productListQueryVo.getEndPrice());
        }
        List<Product> products = productLambdaQueryChainWrapper
                .orderBy(true, false, Product::getUpdateTime)
                .list();
        List<Store> stores = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(products)) {
            stores.addAll(storeDao.lambdaQuery()
                    .in(Store::getId, products.stream().map(Product::getStoreId).collect(Collectors.toList()))
                    .list());
        }
        List<ProductListVo> productListVos = Lists.newArrayList();
        List<User> users = userDao.lambdaQuery().list();
        products.forEach(temp -> {
            ProductListVo productListVo = new ProductListVo();
            BeanUtils.copyProperties(temp, productListVo);
            Optional<Store> optional = stores.stream()
                    .filter(tempStore -> tempStore.getId().equals(temp.getStoreId()))
                    .findFirst();
            optional.ifPresent(o -> {
                productListVo.setStoreId(o.getId());
                productListVo.setStoreName(o.getName());
                productListVo.setMerchantId(o.getUserId());
                Optional<String> optionalS = users.stream()
                        .filter(tempUser -> tempUser.getId().equals(o.getUserId()))
                        .map(User::getNickname)
                        .findFirst();
                optionalS.ifPresent(productListVo::setMerchantName);
            });
            productListVos.add(productListVo);
        });
        return new ProductListPageVo(productListVos.size(),
                productListVos.stream()
                        .skip((long) (productListQueryVo.getPageIndex() - 1) * productListQueryVo.getPageSize())
                        .limit(productListQueryVo.getPageSize())
                        .collect(Collectors.toList()));
    }

    @Override
    public void uploadAvatar(MultipartFile multipartFile, String id) {
        String absolutePath = FileUtils.uploadFile(multipartFile, filePath, multipartFile.getOriginalFilename());
        productDao.lambdaUpdate()
                .eq(Product::getId, id)
                .set(Product::getFirstImage, multipartFile.getOriginalFilename())
                .update();
    }

    @Override
    public List<Product> queryFirstPageList() {
        List<Product> products = productDao.lambdaQuery().orderBy(true, false, Product::getSales).list();
        return products.stream().skip(0L).limit(6L).collect(Collectors.toList());
    }
}
