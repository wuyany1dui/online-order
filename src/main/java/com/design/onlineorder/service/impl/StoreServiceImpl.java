package com.design.onlineorder.service.impl;

import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.enums.UserTypeEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.StoreService;
import com.design.onlineorder.utils.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Created by DrEAmSs on 2022-04-26 13:17
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Resource
    private StoreDao storeDao;

    @Override
    public void create(Store store) {
        if (!Objects.equals(UserUtils.getCurrentUser().getType(), UserTypeEnum.MERCHANT)) {
            throw new MyException(400, ResultEnum.ACCESS_DENIED.getLabel());
        }
        Optional<Store> storeOptId = storeDao.lambdaQuery()
                .eq(Store::getUserId, UserUtils.getCurrentUser().getId())
                .oneOpt();
        if (storeOptId.isPresent()) {
            throw new MyException(400, ResultEnum.TOO_MANY_STORES.getLabel());
        }
        Optional<Store> storeOptName = storeDao.lambdaQuery().eq(Store::getName, store.getName()).oneOpt();
        if (storeOptName.isPresent()) {
            throw new MyException(400, ResultEnum.STORE_NAME_EXISTS.getLabel());
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        store.setCreateTime(timestamp);
        store.setUpdateTime(timestamp);
        storeDao.save(store);
    }

    @Override
    public Store query() {
        Optional<Store> store = storeDao.lambdaQuery()
                .eq(Store::getUserId, UserUtils.getCurrentUser().getId())
                .oneOpt();
        return store.orElse(null);
    }
}
