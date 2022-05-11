package com.design.onlineorder.service;

import com.design.onlineorder.entity.User;
import com.design.onlineorder.vo.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Created by DrEAmSs on 2022-04-25 17:52
 */
public interface UserService {

    /**
     * 用户注册
     */
    void register(User user);

    /**
     * 用户登录返回token
     */
    String login(UserLoginVo data);

    /**
     * 商家用户登录返回token
     */
    String storeLogin(UserLoginVo data);

    /**
     * 按照id查询用户
     */
    UserVo queryById(String userId);

    /**
     * 上传用户头像
     */
    void uploadAvatar(MultipartFile multipartFile);

    /**
     * 修改用户信息
     */
    void modifyUserInfo(UserVo userVo);

    /**
     * 修改用户密码
     */
    void modifyUserPassword(ModifyPasswordVo modifyPasswordVo);

    /**
     * 查询用户列表
     */
    UserPageVo queryList(UserQueryVo userQueryVo);

    /**
     * 修改用户权限
     * @param id
     */
    void modifyLevel(ModifyLevelVo modifyLevelVo);
}
