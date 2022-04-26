package com.design.onlineorder.config;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @author DrEAmSs
 * 全局处理时间戳转化为Date
 */
public class LongDateConverter implements Converter<Long, Date> {

    @Override
    public Date convert(Long value) {
        return new Date(value);
    }

}
