package com.happycoding.music.config.properties;

import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/17 10:42
 */
@Data
public class SystemProperties {

    private Boolean singleLogin = true;

    private Boolean enableCaptcha = true;
}
