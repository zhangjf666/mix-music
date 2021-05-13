package com.happycoding.music.model;

import lombok.Data;

import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/13 14:04
 */
@Data
public class MiguOption {

    private String dataType;

    private Map<String,String> header;
}
