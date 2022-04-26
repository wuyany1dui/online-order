package com.design.onlineorder.config;

import com.design.onlineorder.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author DrEAmSs
 * 全局处理枚举类
 */
public class BaseEnumConverterFactory implements ConverterFactory<String, BaseEnum<?>> {

    @Override
    public <T extends BaseEnum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            for (T t : targetType.getEnumConstants()) {
                if (t.getValue().toString().equals(source)) {
                    return t;
                }
            }
            return null;
        };
    }
}
