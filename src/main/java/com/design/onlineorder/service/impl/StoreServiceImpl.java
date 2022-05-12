package com.design.onlineorder.service.impl;

import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.enums.StoreStatusEnum;
import com.design.onlineorder.enums.UserTypeEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.StoreService;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.StoreListQueryVo;
import com.design.onlineorder.vo.StorePageVo;
import com.design.onlineorder.vo.StoreVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Created by DrEAmSs on 2022-04-26 13:17
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Resource
    private StoreDao storeDao;

    @Override
    public void create(Store store) {
        if (!Objects.equals(UserUtils.getCurrentUser().getType(), UserTypeEnum.MERCHANT) &&
                !Objects.equals(UserUtils.getCurrentUser().getType(), UserTypeEnum.ADMIN)) {
            throw new MyException(400, ResultEnum.ACCESS_DENIED.getLabel());
        }
        Optional<Store> storeOptName = storeDao.lambdaQuery().eq(Store::getName, store.getName()).oneOpt();
        if (storeOptName.isPresent()) {
            throw new MyException(400, ResultEnum.STORE_NAME_EXISTS.getLabel());
        }
        if (StringUtils.isNotBlank(store.getId())) {
            store.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            store.setId(UUID.randomUUID().toString());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            store.setCreateTime(timestamp);
            store.setUpdateTime(timestamp);
        }
        store.setStatus(StoreStatusEnum.NORMAL);
        storeDao.saveOrUpdate(store);
    }

    @Override
    public Store query() {
        Optional<Store> store = storeDao.lambdaQuery()
                .eq(Store::getUserId, UserUtils.getCurrentUser().getId())
                .oneOpt();
        return store.orElse(null);
    }

    @Override
    public StorePageVo queryList(StoreListQueryVo storeListQueryVo) {
        List<Store> stores = storeDao.lambdaQuery()
                .eq(StringUtils.isNotBlank(storeListQueryVo.getUserId()), Store::getUserId, storeListQueryVo.getUserId())
                .like(StringUtils.isNotBlank(storeListQueryVo.getType()), Store::getType, storeListQueryVo.getType())
                .list();
        return new StorePageVo(stores.size(), stores.stream()
                .skip((long) storeListQueryVo.getPageSize() * (storeListQueryVo.getPageIndex() - 1))
                .limit(storeListQueryVo.getPageSize())
                .collect(Collectors.toList()));
    }

    @Override
    public List<StoreVo> queryFirstPageList() {
        List<Store> stores = storeDao.lambdaQuery().orderBy(true, true, Store::getSort).list();
        return stores.stream()
                .map(tempStore -> new StoreVo(tempStore.getId(), tempStore.getName(), tempStore.getDescription()))
                .collect(Collectors.toList());
    }
}
