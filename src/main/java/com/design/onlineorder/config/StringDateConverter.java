package com.design.onlineorder.config;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @author DrEAmSs
 * 全局处理String字符串时间戳转化为Timestamp
 */
public class StringDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String value) {
        if (value.isEmpty()) {
            return null;
        }
        long timestamp = Long.parseLong(value);
        return new Date(timestamp);
    }
}
