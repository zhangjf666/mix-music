package com.happycoding.music.common.support.mapstruct;


import static com.happycoding.music.common.constants.Constants.BOOL_FALSE;
import static com.happycoding.music.common.constants.Constants.BOOL_TRUE;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/12 16:58
 */
public class MapStructTransform {

    public String booleanToString(boolean value) {
        return value ? BOOL_TRUE : BOOL_FALSE;
    }

    public boolean stringToBoolean(String value) {
        return BOOL_TRUE.equals(value);
    }
}
