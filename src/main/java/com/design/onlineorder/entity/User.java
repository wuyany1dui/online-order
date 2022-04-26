package com.design.onlineorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.design.onlineorder.enums.UserTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:00
 */
@Data
@TableName("t_user")
public class User {

    /**
     * 用户主键id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码 AES加密
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 手机号码
     */
    private Long phoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 注册时间
     */
    private Timestamp registrationTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 头像图片地址
     */
    private String avatar;

    /**
     * 用户类型 0：普通用户 1：商家 2：系统管理员
     */
    private UserTypeEnum type;
}
