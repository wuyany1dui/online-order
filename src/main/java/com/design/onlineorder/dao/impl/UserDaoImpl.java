package com.design.onlineorder.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.design.onlineorder.dao.UserDao;
import com.design.onlineorder.entity.User;
import com.design.onlineorder.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:12
 */
@Service
public class UserDaoImpl extends ServiceImpl<UserMapper, User> implements UserDao {
}
