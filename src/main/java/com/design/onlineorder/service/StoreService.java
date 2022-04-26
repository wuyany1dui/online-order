package com.design.onlineorder.service;

import com.design.onlineorder.entity.Store;

/**
 * @author Created by DrEAmSs on 2022-04-26 13:16
 */
public interface StoreService {

    /**
     * 创建店铺
     */
    void create(Store store);

    /**
     * 获取当前用户的商店信息
     */
    Store query();
}
