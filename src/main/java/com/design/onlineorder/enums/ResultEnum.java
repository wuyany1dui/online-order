package com.design.onlineorder.enums;

import lombok.Getter;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:51
 */
@Getter
public enum ResultEnum implements BaseEnum<Integer> {

    SUCCESS(0, "成功"),
    LOGIN_FAIL(1, "用户名或密码错误"),
    USERNAME_NOT_EXISTS(2, "用户名不存在"),
    SERVER_ERROR(3, "服务器内部错误"),
    TOKEN_ILLEGAL(4, "token不合法"),
    TOKEN_PARSE_ERROR(5, "token解析失败"),
    USER_UNVERIFIED(6, "用户未验证"),
    PWD_INCONSISTENT(7, "当前密码错误"),
    USERNAME_EXISTS(8, "用户名已存在"),
    ACCESS_DENIED(9, "用户权限不足"),
    TOO_MANY_STORES(10, "一个用户只能创建一个店铺"),
    STORE_NAME_EXISTS(11, "店铺名已存在");

    private final Integer value;
    private final String label;

    ResultEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
