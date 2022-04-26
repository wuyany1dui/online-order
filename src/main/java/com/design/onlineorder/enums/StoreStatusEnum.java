package com.design.onlineorder.enums;

import lombok.Getter;

/**
 * @author Created by DrEAmSs on 2022-04-26 18:44
 * 商店状态枚举类
 */
@Getter
public enum StoreStatusEnum implements BaseEnum<Integer> {

    NORMAL(0, "正常"),
    BANNED(1, "封禁中");

    private final Integer value;
    private final String label;

    StoreStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
