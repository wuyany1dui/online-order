package com.design.onlineorder.enums;

import lombok.Getter;

/**
 * @author Created by DrEAmSs on 2022-04-25 19:05
 * 用户类型枚举类
 */
@Getter
public enum UserTypeEnum implements BaseEnum<Integer> {

    NORMAL(0, "普通用户"),
    MERCHANT(1, "商家"),
    ADMIN(2, "系统管理员");

    private final Integer value;
    private final String label;

    UserTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}