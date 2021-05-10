package com.happycoding.music.common.support.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/9 15:29
 */
public interface JsonMapper {
    Map toMap(String json);

    List toList(String json);

    <T> List<T> toList(String json, final Type type);

    String toJsonString(Object object);

    String toJsonWithDateFormat(Object object, String dateFormatPattern);

    <T> T toPojo(String json, Class<T> valueType);

    Map convertToMap(Object fromValue);

    <T> T convertFromMap(Map fromMap, Class<T> toValueType);
}
