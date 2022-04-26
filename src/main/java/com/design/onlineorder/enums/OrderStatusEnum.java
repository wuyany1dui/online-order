package com.design.onlineorder.enums;

import lombok.Getter;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:16
 * 订单状态枚举类
 */
@Getter
public enum OrderStatusEnum implements BaseEnum<Integer> {

    NOT_PAID(0, "未支付"),
    PAID(1, "已支付"),
    OVERTIME(2, "已超期");

    private final Integer value;
    private final String label;

    OrderStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
