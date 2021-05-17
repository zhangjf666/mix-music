package com.happycoding.music.config;

import com.happycoding.music.common.base.IEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 枚举转换类
 * @Date: 2021/5/17 16:20
 */
public class EnumConvertFactory implements ConverterFactory<String, IEnum> {
    @Override
    public <T extends IEnum> Converter<String, T> getConverter(Class<T> aClass) {
        return new StringToIEum<>(aClass);
    }

    @SuppressWarnings("all")
    private static class StringToIEum<T extends IEnum> implements Converter<String, T> {
        private Class<T> targerType;
        public StringToIEum(Class<T> targerType) {
            this.targerType = targerType;
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isEmpty(source)) {
                return null;
            }
            return (T) EnumConvertFactory.getEnum(this.targerType, source);
        }
    }

    private static <T extends IEnum> Object getEnum(Class<T> targerType, String source) {
        for (T enumObj : targerType.getEnumConstants()) {
            if (source.equals(String.valueOf(enumObj.getId()))) {
                return enumObj;
            }
        }
        return null;
    }
}
