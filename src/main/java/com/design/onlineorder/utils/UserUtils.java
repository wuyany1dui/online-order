package com.design.onlineorder.utils;

import com.design.onlineorder.vo.UserVo;
import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-04-26 10:54
 * 将用户信息放到ThreadLocal
 */
@Data
public class UserUtils {

    private static final ThreadLocal<UserVo> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserVo userVo) {
        currentUser.set(userVo);
    }

    public static UserVo getCurrentUser() {
        return currentUser.get();
    }
}
