package com.design.onlineorder.enums;

import lombok.Getter;

/**
 * @author Created by DrEAmSs on 2022-04-26 18:41
 * 商品状态枚举类
 */
@Getter
public enum ProductStatusEnum implements BaseEnum<Integer> {

    ON_SALE(0, "上架"),
    OFF_SALE(1, "下架");

    private final Integer value;
    private final String label;

    ProductStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
