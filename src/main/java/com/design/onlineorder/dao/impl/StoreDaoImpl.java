package com.design.onlineorder.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.mapper.StoreMapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:12
 */
@Service
public class StoreDaoImpl extends ServiceImpl<StoreMapper, Store> implements StoreDao {
}
