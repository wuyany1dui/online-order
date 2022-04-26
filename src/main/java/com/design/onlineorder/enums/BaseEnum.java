package com.design.onlineorder.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author DrEAmSs
 * 基础枚举类
 */
public interface BaseEnum<T extends Serializable> extends IEnum<T> {

    String getLabel();

    @JsonValue
    default T toJsonValue() {
        return getValue();
    }
}
