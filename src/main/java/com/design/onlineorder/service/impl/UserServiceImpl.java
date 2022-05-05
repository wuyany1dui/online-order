package com.design.onlineorder.service.impl;

import com.design.onlineorder.dao.UserDao;
import com.design.onlineorder.entity.User;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.enums.UserTypeEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.UserService;
import com.design.onlineorder.utils.FileUtils;
import com.design.onlineorder.utils.JwtUtils;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.ModifyPasswordVo;
import com.design.onlineorder.vo.UserLoginVo;
import com.design.onlineorder.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Created by DrEAmSs on 2022-04-25 17:52
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Value("${onlineOrder.filePath}")
    private String filePath;

    @Override
    public void register(User user) {
        Optional<User> userOpt = userDao.lambdaQuery().eq(User::getUsername, user.getUsername()).oneOpt();
        if (userOpt.isPresent()) {
            throw new MyException(400, ResultEnum.USERNAME_EXISTS.getLabel());
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user.setRegistrationTime(timestamp);
        user.setUpdateTime(timestamp);
        user.setDeleted(0);
        userDao.save(user);
    }

    @Override
    public String login(UserLoginVo data) {
        User user = userDao.lambdaQuery().eq(User::getUsername, data.getUsername()).one();
        if (Objects.isNull(user)) {
            throw new MyException(400, ResultEnum.USERNAME_NOT_EXISTS.getLabel());
        }
        if (user.getPassword().equals(data.getPassword())) {
            return JwtUtils.createToken(user.getId(), user.getNickname(), user.getUsername());
        } else {
            throw new MyException(400, ResultEnum.LOGIN_FAIL.getLabel());
        }
    }

    @Override
    public String storeLogin(UserLoginVo data) {
        User user = userDao.lambdaQuery().eq(User::getUsername, data.getUsername()).one();
        if (Objects.isNull(user)) {
            throw new MyException(400, ResultEnum.USERNAME_NOT_EXISTS.getLabel());
        }
        if (Objects.equals(user.getType(), UserTypeEnum.MERCHANT)) {
            throw new MyException(400, ResultEnum.ACCESS_DENIED.getLabel());
        }
        if (user.getPassword().equals(data.getPassword())) {
            return JwtUtils.createToken(user.getId(), user.getNickname(), user.getUsername());
        } else {
            throw new MyException(400, ResultEnum.LOGIN_FAIL.getLabel());
        }
    }

    @Override
    public UserVo queryById(String userId) {
        User user = userDao.lambdaQuery().eq(User::getId, userId).one();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public void uploadAvatar(MultipartFile multipartFile) {
        String absolutePath = FileUtils.uploadFile(multipartFile, filePath, multipartFile.getOriginalFilename());
        userDao.lambdaUpdate()
                .eq(User::getId, UserUtils.getCurrentUser().getId())
                .set(User::getAvatar, absolutePath)
                .update();
    }

    @Override
    public void modifyUserInfo(UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        userDao.saveOrUpdate(user);
    }

    @Override
    public void modifyUserPassword(ModifyPasswordVo modifyPasswordVo) {
        User user = userDao.lambdaQuery().eq(User::getId, UserUtils.getCurrentUser().getId()).one();
        if (!user.getPassword().equals(modifyPasswordVo.getOldPassword())) {
            throw new MyException(400, ResultEnum.PWD_INCONSISTENT.getLabel());
        } else {
            userDao.lambdaUpdate()
                    .eq(User::getId, UserUtils.getCurrentUser().getId())
                    .set(User::getPassword, modifyPasswordVo.getNewPassword())
                    .update();
        }
    }
}
